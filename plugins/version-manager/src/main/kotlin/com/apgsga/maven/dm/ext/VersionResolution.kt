package com.apgsga.maven.dm.ext


import com.apgsga.gradle.repo.extensions.RepoType
import com.apgsga.gradle.repo.extensions.Repos
import com.apgsga.gradle.repo.plugin.ApgCommonRepoPlugin
import com.apgsga.maven.VersionResolver
import com.apgsga.maven.impl.resolver.BomVersionGradleResolverBuilder
import com.apgsga.maven.impl.resolver.BomVersionResolverBuilder
import com.apgsga.maven.impl.resolver.CompositeVersionResolverBuilder
import com.apgsga.maven.impl.resolver.PatchFileVersionResolverBuilder
import com.apgsga.revision.manager.domain.RevisionManager
import com.apgsga.revision.manager.domain.RevisionManagerBuilder
import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getByName
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun version(baseVersion: String?, revision: String?): String {
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
    var persistence: RevisionManagerBuilder.PersistenceTyp = RevisionManagerBuilder.PersistenceTyp.BEANS
    var algorithm: RevisionManagerBuilder.AlgorithmTyp = RevisionManagerBuilder.AlgorithmTyp.SNAPSHOT
    var installTarget: String? = null
    var bomDestDirPath: String = "${project.buildDir}/generatedBom"
    private var _versionResolver : VersionResolver? = null
    val versionResolver: VersionResolver
        get() {
            if (_versionResolver == null) {
                _versionResolver = buildVersionResolver()
            }
            return _versionResolver as VersionResolver
        }
    // Revision Manager and Revision Initialization
    private val revisionManger: RevisionManager get() = initRevisionManager()
    var revisionRootPath: String = project.gradle.gradleUserHomeDir.absolutePath
    private var _bomNextRevision: String? = null
    val bomNextRevision: String
        get() {
            if (_bomNextRevision == null) {
                _bomNextRevision = revisionManger.nextRevision().toString()
            }
            return _bomNextRevision as String
        }
    private var _bomLastRevision: String? = null
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
        return revisionManagerBuilder.revisionRootPath(revisionRootPath).algorithm(algorithm).persistence(persistence).build()

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

    fun generateBom(configurationsClosure: Closure<Any>) {
        val callingClosure = configurationsClosure.delegate as Closure<*>
        val mavenPublication = callingClosure.delegate as MavenPublication
        generateBomXml(mavenPublication)
    }

    private fun generateBomXml(publication: MavenPublication) {
        publication.artifactId = bomArtifactId
        publication.groupId = bomGroupId
        publication.version = version(bomNextRevision)
        publication.pom {
            name.set(bomArtifactId)
            val date = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy hh:mm:ss")
            val dateFormatted = date.format(formatter)
            description.set("Generated by Gradle Task $this at $dateFormatted")
        }

        publication.pom.withXml {
            val dm = asNode().appendNode("dependencyManagement")
            val dependenciesNode = dm.appendNode("dependencies")
            versionResolver.getMavenArtifactList()?.forEach {
                val dependencyNode = dependenciesNode.appendNode("dependency")
                dependencyNode.appendNode("groupId", it.groupId)
                dependencyNode.appendNode("artifactId" , it.artifactId)
                dependencyNode.appendNode("version", it.version)
                // TODO (che, jhe,12.3) : possibly more
//                dependencyNode.appendNode("scope", it.scope)
//                dependencyNode.appendNode("classifier", it.classifier)
//                dependencyNode.appendNode("classifier", it.classifier)
            }
        }
    }

    private fun buildVersionResolver(): VersionResolver {
        assert(bomArtifactId != null) { "bomArtifactId should not be null" }
        assert(bomGroupId != null) { "bomGroupId should not be null" }
        assert(bomBaseVersion != null) { "bomBaseVersion should not be null" }
        assert(bomLastRevision != null) { "lastRevision should not be null" }
        val compositeResolverBuilder = CompositeVersionResolverBuilder()
        project.logger.info("Creating Dependency configuration")
        var cnt = 0
        if (!patches.fileNames.isNullOrEmpty()) {
            patches.fileNames?.split(':')?.forEach {
                project.logger.info("Patchfile : $it.toString()")
                compositeResolverBuilder.add(++cnt, PatchFileVersionResolverBuilder()
                        .parentDir(patches.parentDir!!)
                        .patchFile(it))
            }
        }
        project.logger.info("Version: ${bomLastRevision?.let { version(it) }}")
        compositeResolverBuilder.add(++cnt, BomVersionGradleResolverBuilder()
                .bomArtifact("${bomGroupId}:${bomArtifactId}:${bomLastRevision?.let { version(it) }}")
                .recursive(true))
        return compositeResolverBuilder.build(project)
    }


    fun version(revision: String): String {
        return version(bomBaseVersion, revision)
    }

    override fun toString(): String {
        return "VersionResolutionExtension(configurationName='$configurationName', bomArtifactId=$bomArtifactId, bomGroupId=$bomGroupId, bomBaseVersion=$bomBaseVersion, revisionTyp=$persistence, installTarget=$installTarget, patches=$patches)"
    }


}