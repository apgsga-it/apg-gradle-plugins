package com.apgsga.gradle.maven.dm.tasks

import com.apgsga.gradle.maven.dm.plugin.apgDmBombReportExt
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

open class DmBomReportTask : DefaultTask() {

    @TaskAction
    fun doAction() {
        logger.log(LogLevel.INFO, "Reporting started")
        project.apgDmBombReportExt().log()
        logger.log(LogLevel.INFO, "Reporting done.")

    }
}