package com.apgsga.maven.dm.ext


import org.gradle.api.Action
import org.gradle.api.Project


data class Boms( var artifactId: String, var groupId : String,  var versions: String)

class Patches {
    var parentDir: String? = null
    var fileNames: String? = null
}

open class VersionResolutionExtension(val project: Project) {
    var configurationName: String = "serviceRuntime"
        set(value) {
            if (configurationName != value) configureConfiguration(configurationName)
            field = value
        }
    var boms : String = ""
    val patches: Patches = Patches()
    init {
        configureConfiguration(configurationName)
    }

    fun patches(action: Action<Patches>) {
        action.execute(patches)
    }

    fun log() {
        project.logger.info("Logging VersionResolutions: ")
        project.logger.info(boms)
        project.logger.info(patches.toString())
    }

    private fun configureConfiguration(name : String) {
        val config = project.configurations.findByName(name)
        if (config == null) {
            project.configurations.create(name)
        }
    }
}