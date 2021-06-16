package com.apgsga.maven.dm.plugin

import com.apgsga.common.repo.plugin.ApgCommonRepoPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class MavenBomManagerPlugin : Plugin<Project> {
    /**
     * Apply this plugin to the given target object.
     *
     * @param target The target object
     */
    override fun apply(project: Project) {
        project.plugins.apply(ApgCommonRepoPlugin::class.java)
        project.tasks.create("bomAnalyzerTask",BomAnalyzerTask::class.java)

    }
}