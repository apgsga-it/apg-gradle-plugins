package com.apgsga.maven.dm.plugin

import com.apgsga.gradle.repo.plugin.ApgCommonRepoPlugin
import com.apgsga.maven.dm.ext.VersionResolutionExtension
import com.apgsga.maven.dm.tasks.ResolutionStrategyConfigTask
import com.apgsga.revision.manager.domain.RevisionManagerBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project


open class VersionResolutionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(ApgCommonRepoPlugin::class.java)
        val revisionManagerBuilder = RevisionManagerBuilder.create()
        project.extensions.create("apgVersionResolver", VersionResolutionExtension::class.java, project, revisionManagerBuilder)
        project.tasks.register("configureResolutionStrategy", ResolutionStrategyConfigTask::class.java)

    }

}