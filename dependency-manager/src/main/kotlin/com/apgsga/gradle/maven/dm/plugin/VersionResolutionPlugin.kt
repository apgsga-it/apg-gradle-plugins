@file:Suppress("UnstableApiUsage")

package com.apgsga.gradle.maven.dm.plugin

import com.apgsga.gradle.maven.dm.ext.PatchVersionResolution
import com.apgsga.gradle.maven.dm.ext.PomVersionResolution
import com.apgsga.gradle.maven.dm.ext.VersionResolutionExtension
import com.apgsga.gradle.maven.dm.tasks.ResolutionStrategyConfigTask
import com.apgsga.gradle.repo.plugin.ApgCommonRepoPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project


open class VersionResolutionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(ApgCommonRepoPlugin::class.java)
        val boms = project.container(PomVersionResolution::class.java)
        val patches  = project.container(PatchVersionResolution::class.java)
        project.extensions.create("versionResolver", VersionResolutionExtension::class.java, project, boms, patches)
        project.tasks.register("configureResolutionStrategy", ResolutionStrategyConfigTask::class.java)
    }
}