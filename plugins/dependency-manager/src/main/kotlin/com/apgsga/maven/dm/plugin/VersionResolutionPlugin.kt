package com.apgsga.maven.dm.plugin

import com.apgsga.gradle.repo.plugin.ApgCommonRepoPlugin
import com.apgsga.maven.dm.ext.VersionResolutionExtension
import com.apgsga.maven.dm.tasks.ResolutionStrategyConfigTask
import org.gradle.api.Plugin
import org.gradle.api.Project


open class VersionResolutionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(ApgCommonRepoPlugin::class.java)
        project.extensions.create("versionResolvers", VersionResolutionExtension::class.java, project)
        project.tasks.register("configureResolutionStrategy", ResolutionStrategyConfigTask::class.java)
    }
}