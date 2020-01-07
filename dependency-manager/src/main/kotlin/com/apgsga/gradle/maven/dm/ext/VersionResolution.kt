package com.apgsga.gradle.maven.dm.ext


import org.gradle.api.Action
import org.gradle.api.Project


class Boms {
    var artifactId: String? = null
    var groupId: String? = null
    var versions: String? = null
}

class Patches {
    var parentDir: String? = null
    var fileNames: String? = null
}


open class VersionResolutionExtension(val project: Project) {
    var configurationName: String = "runtime"
    val boms: Boms = Boms()
    val patches: Patches = Patches()

    fun boms(action: Action<Boms>) {
        action.execute(boms)
    }

    fun patches(action: Action<Patches>) {
        action.execute(patches)
    }

    fun log() {
        project.logger.info("Logging VersionResolutions: ")
        project.logger.info(boms.toString())
        project.logger.info(patches.toString())
    }
}