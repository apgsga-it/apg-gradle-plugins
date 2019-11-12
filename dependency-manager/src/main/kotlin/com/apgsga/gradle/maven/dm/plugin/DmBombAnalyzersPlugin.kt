package com.apgsga.gradle.maven.dm.plugin

import com.apgsga.gradle.maven.dm.ext.DmBomAnalyizerExtension
import com.apgsga.gradle.maven.dm.tasks.DmBomReportTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register


open class DmBombAnalyzersPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val ext = target.extensions
        ext.create("apgDmBomReportConfig", DmBomAnalyizerExtension::class, target)
        target.tasks.register("apgDmBomReport", DmBomReportTask::class)

    }
}