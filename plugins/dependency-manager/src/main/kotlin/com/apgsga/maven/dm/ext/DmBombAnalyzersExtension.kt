package com.apgsga.maven.dm.ext

import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

open class DmBombAnalyzersExtension(private val project : Project) {

    private val boms = listOf<String>()

    fun log() {
        project.logger.log(LogLevel.INFO,"Boms to analyze: $boms")
    }

}