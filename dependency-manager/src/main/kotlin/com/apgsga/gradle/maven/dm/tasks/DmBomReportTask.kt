package com.apgsga.gradle.maven.dm.tasks

import com.apgsga.maven.impl.MavenBomManagerDefault
import com.apgsga.maven.impl.RepositoryFactory
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class DmBomReportTask : DefaultTask() {


    @Input
    var boms: Collection<String> = listOf<String>()

    @TaskAction
    fun doAction() {
        logger.log(LogLevel.INFO, "Reporting started")
        val bomManager = MavenBomManagerDefault(RepositoryFactory.createFactory("build/repo","bom-test").makeRepo())
        boms.forEach{
            logger.info("Reporting for artifactid: $it")
            val result = bomManager.retrieve(it)
            logger.info("Result: $result")
        }
        logger.log(LogLevel.INFO, "Reporting done.")

    }


}