package com.apgsga.gradle.maven.dm.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

open class DmBomReportTask : DefaultTask() {

    @TaskAction
    fun doAction() {
        logger.log(LogLevel.INFO, "Reporting started")
        logger.log(LogLevel.INFO, "Reporting done.")

    }
}