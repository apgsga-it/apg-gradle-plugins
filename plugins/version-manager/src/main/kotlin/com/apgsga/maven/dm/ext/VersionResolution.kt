package com.apgsga.maven.dm.ext


import com.apgsga.revision.manager.domain.RevisionManager
import com.apgsga.revision.manager.domain.RevisionManagerBuilder
import org.gradle.api.Action
import org.gradle.api.Project
import java.lang.IllegalArgumentException

fun version(baseVersion: String?, revision: String?) : String {
    val baseVersionRegex = Regex("[A-Z]*-[A-Z]*$")
    if (baseVersionRegex.containsMatchIn(baseVersion!!)) {
        return "${baseVersion}-${revision}"
    }
    val revisionInclSeperator = if (revision == "SNAPSHOT") "-SNAPSHOT" else ".${revision}"
    val versionNumberRegex = Regex("\\.?[0-9]+$")
    if (versionNumberRegex.containsMatchIn(baseVersion)) {
        return "${baseVersion}${revisionInclSeperator}"
    }
    throw IllegalArgumentException("Illegal Version for Artifact with baseVersion: ${baseVersion} and revision: ${revision}")

}

data class Bom(var artifactId: String?, var groupId: String?, var baseVersion: String?, var revision: String?) {
    fun version() : String {
        return version(baseVersion,revision)
    }
}

class Patches {
    var parentDir: String? = null
    var fileNames: String? = null
}

open class VersionResolutionExtension(val project: Project, private val revisionManagerBuilder: RevisionManagerBuilder) {
    var configurationName: String = "serviceRuntime"
        set(value) {
            if (configurationName != value) configureConfiguration(configurationName)
            field = value
        }
    var bomArtifactId: String? = null
    var bomGroupId: String? = null
    var bomBaseVersion: String? = null
    var revisionRootPath: String = project.gradle.gradleUserHomeDir.absolutePath
    var persistence: RevisionManagerBuilder.PersistenceTyp = RevisionManagerBuilder.PersistenceTyp.BEANS
    var algorithm : RevisionManagerBuilder.AlgorithmTyp = RevisionManagerBuilder.AlgorithmTyp.SNAPSHOT
    var installTarget: String? = null
    var bomDestDirPath : String = "${project.buildDir}/generatedBom"

    // Revision Manager and Revision Initialization
    private val revisionManger: RevisionManager get() = initRevisionManager()
    private var _bomNextRevision : String? = null
    val bomNextRevision: String get() {
        if (_bomNextRevision == null) {
            _bomNextRevision = revisionManger.nextRevision().toString()
        }
        return _bomNextRevision as String
    }
    private var _bomLastRevision : String? = null
    var bomLastRevision: String?
        get() {
            if (_bomLastRevision == null) {
                _bomLastRevision = revisionManger.lastRevision(installTarget) as String?
            }
            return _bomLastRevision
        }
        set(value) {
            this._bomLastRevision = value
        }
    val patches: Patches = Patches()

    init {
        configureConfiguration(configurationName)
    }

    private fun initRevisionManager(): RevisionManager {
        // TODO (jhe, che) : consider current revision file and bootstrapping
        return revisionManagerBuilder.
                revisionRootPath(revisionRootPath).algorithm(algorithm).persistence(persistence).build()

    }

    fun saveRevision() {
        revisionManger.saveRevision(installTarget, bomNextRevision, bomBaseVersion)
    }

    fun patches(action: Action<Patches>) {
        action.execute(patches)
    }

    fun log() {
        project.logger.info("Logging VersionResolutions: ")
        project.logger.info(toString())
        project.logger.info(patches.toString())
    }

    private fun configureConfiguration(name: String) {
        val config = project.configurations.findByName(name)
        if (config == null) {
            project.configurations.create(name)
        }
    }

    override fun toString(): String {
        return "VersionResolutionExtension(configurationName='$configurationName', bomArtifactId=$bomArtifactId, bomGroupId=$bomGroupId, bomBaseVersion=$bomBaseVersion, revisionTyp=$persistence, installTarget=$installTarget, patches=$patches)"
    }


}