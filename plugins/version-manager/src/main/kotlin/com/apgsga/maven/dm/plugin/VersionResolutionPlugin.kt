package com.apgsga.maven.dm.plugin

import com.apgsga.maven.dm.ext.VersionResolutionExtension
import com.apgsga.revision.manager.domain.RevisionManagerBuilder
import com.apgsga.common.repo.plugin.ApgCommonRepoPlugin
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ModuleVersionSelector
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin


open class VersionResolutionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(ApgCommonRepoPlugin::class.java)
        project.plugins.apply(MavenPublishPlugin::class.java)
        val revisionManagerBuilder = RevisionManagerBuilder.create()
        val extension = project.extensions.create(
            "apgVersionResolver",
            VersionResolutionExtension::class.java,
            project,
            revisionManagerBuilder
        )

        project.tasks.create("mergeRevision",MergeRevision::class.java)

        project.afterEvaluate {
            applyRecommendations(project, extension)
        }
    }

    private fun applyRecommendations(project: Project, resolutionExtension: VersionResolutionExtension) {
        val config = project.configurations.findByName(resolutionExtension.configurationName)
        val artifactMaps = mutableMapOf<String, MutableList<Pair<String, String>>>()
        val action = Action<Configuration> {
            if (this == config) {
                if (state == Configuration.State.UNRESOLVED) {
                    resolutionStrategy.eachDependency {
                        val calculatedVersion =
                            resolutionExtension.versionResolver.getVersion(requested.group, requested.name)
                        var versionUsed = calculatedVersion
                        if (versionUsed.isEmpty() && !requested.version.isNullOrEmpty()) versionUsed =
                            requested.version!!
                        project.logger.info("Resolving Version for Artifact: groupId <${requested.group}> , artifactId <${requested.name}> , version: <${requested.version}>, calculated version: <${calculatedVersion}>, version used: <${versionUsed}>")
                        checkDuplicates(project, artifactMaps, requested, versionUsed)
                        this.useVersion(versionUsed)
                        this.because("Apg Version Resolver, resolved Artifact: groupId <${requested.group}> , artifactId <${requested.name}> , version: <${requested.version}>, calculated version: <${calculatedVersion}>, version used: <${versionUsed}>")
                    }

                }
            }

        }
        project.configurations.all(action)
    }


    private fun checkDuplicates(
        project: Project,
        artifactMaps: MutableMap<String, MutableList<Pair<String, String>>>,
        requested: ModuleVersionSelector,
        versionUsed: String
    ) {
        var duplicates = false
        artifactMaps[requested.name]?.forEach() {
            if (it.first != requested.group || it.second != versionUsed) {
                project.logger.warn("!!!!!!!! For artifact with id: ${requested.name} duplicates have been found:  groupIds: <${it.first}, ${requested.group}, versions: <${it.second}, ${versionUsed}>")
                duplicates = true
            }
        }
        if (duplicates) {
            artifactMaps[requested.name]?.add(Pair(requested.group, versionUsed))
        }
        if (!artifactMaps.contains(requested.name)) {
            artifactMaps[requested.name] = mutableListOf(Pair(requested.group, versionUsed))
        }
    }


}



