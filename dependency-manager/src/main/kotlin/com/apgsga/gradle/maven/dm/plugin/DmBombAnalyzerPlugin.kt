@file:Suppress("UnstableApiUsage")

package com.apgsga.gradle.maven.dm.plugin

import com.apgsga.gradle.maven.dm.ext.DmBombAnalyzersExtension
import com.apgsga.gradle.maven.dm.tasks.DmBomReportTask
import org.gradle.api.Plugin
import org.gradle.api.Project

private const val EXTENSION_NAME = "apgDmBomReportConfig"
private const val TASK_NAME = "apgDmBomReport"


open class DmBombAnalyzerPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val ext = target.extensions
        ext.create(EXTENSION_NAME, DmBombAnalyzersExtension::class.java, target)
        target.tasks.register(TASK_NAME, DmBomReportTask::class.java)

    }
}

internal fun Project.apgDmBombReportExt(): DmBombAnalyzersExtension =
        extensions.getByName(EXTENSION_NAME) as? DmBombAnalyzersExtension
                ?: throw IllegalStateException("$EXTENSION_NAME is not of the correct type")