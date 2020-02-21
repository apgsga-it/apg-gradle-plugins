package com.apgsga.maven.dm.tasks

import com.apgsga.gradle.repo.extensions.RepoType
import com.apgsga.gradle.repo.extensions.Repos
import com.apgsga.gradle.repo.plugin.ApgCommonRepoPlugin
import com.apgsga.maven.VersionResolver
import com.apgsga.maven.dm.ext.Bom
import com.apgsga.maven.dm.ext.Patches
import com.apgsga.maven.dm.ext.VersionResolutionExtension
import com.apgsga.maven.impl.resolver.BomVersionResolverBuilder
import com.apgsga.maven.impl.resolver.CompositeVersionResolverBuilder
import com.apgsga.maven.impl.resolver.PatchFileVersionResolverBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByName

open class ResolutionStrategyConfigTask : DefaultTask() {
    @TaskAction
    fun doAction() {
        val resolutionExtension = project.extensions.getByType(VersionResolutionExtension::class.java)
        assert(resolutionExtension.bomArtifactId != null) { "bomArtifactId should not be null" }
        assert(resolutionExtension.bomGroupId != null) { "bomGroupId should not be null" }
        assert(resolutionExtension.bomBaseVersion != null) { "bomBaseVersion should not be null" }
        assert(resolutionExtension.lastRevision() != null) { "lastRevision should not be null" }
        val bom = Bom(resolutionExtension.bomArtifactId, resolutionExtension.bomGroupId, resolutionExtension.bomBaseVersion, resolutionExtension.lastRevision())
        val config = project.configurations.findByName(resolutionExtension.configurationName)
        val versionResolver = buildVersionResolver(project,resolutionExtension.patches, bom)
        config?.resolutionStrategy?.eachDependency {
            this.useVersion(versionResolver.getVersion(requested.group, requested.name))
            this.because("Apg Version Resolver ")
        }
    }
    private fun buildVersionResolver(project: Project, patches: Patches, bom: Bom) : VersionResolver {
        val commonRepos = project.extensions.getByName<Repos>(ApgCommonRepoPlugin.COMMMON_REPO_EXTENSION_NAME)
        val repo = commonRepos[RepoType.MAVEN]
        val compositeResolverBuilder = CompositeVersionResolverBuilder()
        project.logger.info("Creating Dependency configuration")
        var cnt = 0
        patches.fileNames?.split(':')?.forEach {
            project.logger.info("Patchfile : $it.toString()")
            compositeResolverBuilder.add(++cnt, PatchFileVersionResolverBuilder()
                    .parentDir(patches.parentDir!!)
                    .patchFile(it))
        }
        project.logger.info("Version: ${bom.version()}")
        compositeResolverBuilder.add(++cnt, BomVersionResolverBuilder()
                .bomArtifact("${bom.groupId}:${bom.artifactId}:${bom.version()}")
                .recursive(true)
                .userName(repo.user)
                .password(repo.password)
                .repoBaseUrl(repo.repoBaseUrl)
                .repoName(repo.repoName))
        return compositeResolverBuilder.build()

    }


}