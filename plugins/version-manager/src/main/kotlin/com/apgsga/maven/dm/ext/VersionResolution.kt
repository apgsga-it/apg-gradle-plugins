package com.apgsga.maven.dm.ext


import com.apgsga.revision.manager.domain.RevisionManager
import org.gradle.api.Action
import org.gradle.api.Project


data class Bom(var artifactId: String?, var groupId: String?, var baseVersion: String?, var revision: String) {
    fun version() : String {
        return "${baseVersion}-${revision}"
    }
}

class Patches {
    var parentDir: String? = null
    var fileNames: String? = null
}

open class VersionResolutionExtension(val project: Project, val revisionManger: RevisionManager) {
    var configurationName: String = "serviceRuntime"
        set(value) {
            if (configurationName != value) configureConfiguration(configurationName)
            field = value
        }
    var bomArtifactId : String? = null
    var bomGroupId : String? = null
    var bomBaseVersion : String? = null
    var revisionAlogrithm : String = "SNAPSHOT"
    var installTarget : String? = null
    private var backedNextRevision : String? = null
    private var backedLastRevision : String? = null
    val patches: Patches = Patches()
    init {
        configureConfiguration(configurationName)
    }

    fun nextRevision() : String {
        if (backedNextRevision == null) {
            backedNextRevision = revisionManger.nextRevision() as String
        }
        return backedNextRevision as String;

    }

    fun lastRevision() : String {
        if (backedLastRevision == null) {
            backedLastRevision = revisionManger.lastRevision(installTarget) as String
        }
        return backedNextRevision as String;

    }

    fun saveRevision() {
        revisionManger.saveRevision(installTarget, nextRevision(),bomBaseVersion )
    }
    fun patches(action: Action<Patches>) {
        action.execute(patches)
    }

    fun log() {
        project.logger.info("Logging VersionResolutions: ")
        project.logger.info(toString())
        project.logger.info(patches.toString())
    }

    private fun configureConfiguration(name : String) {
        val config = project.configurations.findByName(name)
        if (config == null) {
            project.configurations.create(name)
        }
    }

    override fun toString(): String {
        return "VersionResolutionExtension(configurationName='$configurationName', bomArtifactId='$bomArtifactId', bomGroupId='$bomGroupId', bomBaseVersion='$bomBaseVersion', revisionAlogrithm='$revisionAlogrithm', installTarget='$installTarget')"
    }
}