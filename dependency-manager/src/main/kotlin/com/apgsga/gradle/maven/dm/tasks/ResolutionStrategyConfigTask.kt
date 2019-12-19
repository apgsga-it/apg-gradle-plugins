package com.apgsga.gradle.maven.dm.tasks

import com.apgsga.gradle.maven.dm.ext.PatchVersionResolution
import com.apgsga.gradle.maven.dm.ext.PomVersionResolution
import com.apgsga.gradle.maven.dm.ext.VersionResolutionExtension
import com.apgsga.gradle.repo.extensions.RepoType
import com.apgsga.gradle.repo.extensions.Repos
import com.apgsga.gradle.repo.plugin.ApgCommonRepoPlugin
import com.apgsga.maven.impl.resolver.BomVersionResolverBuilder
import com.apgsga.maven.impl.resolver.CompositeVersionResolverBuilder
import com.apgsga.maven.impl.resolver.PatchFileVersionResolverBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByName

open class ResolutionStrategyConfigTask : DefaultTask() {



    @TaskAction
    fun doAction() {
        val resolutionExtension = project.extensions.getByType(VersionResolutionExtension::class.java)
        project.configurations.create(resolutionExtension.configurationName) {
            resolveVersionConfiguration(project,resolutionExtension.patches, resolutionExtension.poms)
        }
    }
    private fun Configuration.resolveVersionConfiguration(project: Project, patches: NamedDomainObjectContainer<PatchVersionResolution>, boms: NamedDomainObjectContainer<PomVersionResolution>) {
        val commonRepos = project.extensions.getByName<Repos>(ApgCommonRepoPlugin.COMMMON_REPO_EXTENSION_NAME)
        val repo = commonRepos[RepoType.MAVEN]
        val compositeResolverBuilder = CompositeVersionResolverBuilder()
        project.logger.info("Creating Dependency configuration")
        patches.forEach {
            project.logger.info(it.toString())
            compositeResolverBuilder.add(it.order, PatchFileVersionResolverBuilder()
                    .parentDir(it.parentDirName)
                    .patchFile(it.fileName))
        }
        boms.forEach {
            project.logger.info(it.toString())
            compositeResolverBuilder.add(it.order, BomVersionResolverBuilder()
                    .bomArtifact(it.artifact)
                    .recursive(it.recursive)
                    .userName(repo.user)
                    .password(repo.password)
                    .repoBaseUrl(repo.repoBaseUrl)
                    .repoName(repo.repoName)
            )
        }

        val compositeResolver = compositeResolverBuilder.build()
        resolutionStrategy.eachDependency {
            this.useVersion(compositeResolver.getVersion(this.requested.group, this.requested.name))
            // TODO (che, 18.12) : something more meaningful
            this.because("Apg Version Resolution")
        }
    }


}