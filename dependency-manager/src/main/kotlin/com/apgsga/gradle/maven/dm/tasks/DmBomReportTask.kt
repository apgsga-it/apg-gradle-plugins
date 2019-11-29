package com.apgsga.gradle.maven.dm.tasks

import com.apgsga.dependency.reporting.MavenBomManagerDefaultImpl
import com.apgsga.gradle.repo.extensions.Repo
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class DmBomReportTask : DefaultTask() {


    @Input
    var boms: Collection<String> = listOf<String>()

    @Input
    lateinit var repoConfig : Repo


    @TaskAction
    fun doAction() {
        logger.log(LogLevel.INFO, "Reporting started")
        val bomManager = MavenBomManagerDefaultImpl("build/repo", "bom-test", null, null)
        boms.forEach{
            logger.info("Reporting for artifactid: $it")
            val result = bomManager.retrieve(it)
            logger.info("Result: $result")
        }
        logger.log(LogLevel.INFO, "Reporting done.")

    }


}