package com.apgsga.maven.dm.ext


import com.apgsga.maven.VersionResolver
import com.apgsga.maven.impl.resolver.BomVersionGradleResolverBuilder
import com.apgsga.maven.impl.resolver.CompositeVersionResolverBuilder
import com.apgsga.maven.impl.resolver.PatchFileVersionResolverBuilder
import com.apgsga.packaging.extensions.ApgCommonPackageExtension
import com.apgsga.revision.manager.domain.RevisionManager
import com.apgsga.revision.manager.domain.RevisionManagerBuilder
import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun version(baseVersion: String?, revision: String?): String {
    val baseVersionRegex = Regex("[A-Z]*$")
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
    var algorithm: RevisionManagerBuilder.AlgorithmTyp = RevisionManagerBuilder.AlgorithmTyp.SNAPSHOT
    private var _versionResolver: VersionResolver? = null
    val versionResolver: VersionResolver
        get() {
            if (_versionResolver == null) {
                _versionResolver = buildVersionResolver()
            }
            return _versionResolver as VersionResolver
        }

    // Revision Manager and Revision Initialization
    private var _revisionManger: RevisionManager? = null
    private val revisionManger: RevisionManager
        get() {
            if (_revisionManger == null) {
                _revisionManger = revisionManagerBuilder.revisionRootPath(revisionRootPath
                        ?: project.gradle.gradleUserHomeDir.absolutePath).algorithm(algorithm).build()
            }
            return _revisionManger as RevisionManager
        }

    var revisionRootPath: String? = null
    private var _bomNextRevision: String? = null
    private val bomNextRevision: String
        get() {
            if (_bomNextRevision == null) {
                _bomNextRevision = revisionManger.nextRevision().toString()
            }
            return _bomNextRevision as String
        }
    private var _bomLastRevision: String? = null
    private var _installTarget: String? = null
    private val installTarget: String?
        get() {
            if (_installTarget == null) {
                val pkgExt = project.extensions.getByType(ApgCommonPackageExtension::class.java)
                _installTarget = pkgExt.installTarget
            }
            return  _installTarget
        }
    private var _serviceName: String? = null
    private val serviceName: String?
        get() {
             if (_serviceName == null) {
                 val pkgExt = project.extensions.getByType(ApgCommonPackageExtension::class.java)
                 _serviceName = pkgExt.name
             }
             return  _serviceName
         }
    var bomLastRevision: String?
        get() {
            if (_bomLastRevision == null) {
                _bomLastRevision = revisionManger.lastRevision(serviceName, installTarget)
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

    fun saveRevision() {
        // TODO (che, jhe , 26.3 ) When best to save the Revision? Necessary?
        revisionManger.saveRevision(serviceName, installTarget, bomNextRevision, bomBaseVersion)
    }

    fun patches(action: Action<Patches>) {
        action.execute(patches)
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
        project.gradle.startParameter.taskNames.forEach tasks@{
            project.logger.info("Task : $it")
            if (it.startsWith("publish")) {
                generateBomXml(mavenPublication)
                saveRevision()
                return@tasks
            }
        }
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
                dependencyNode.appendNode("artifactId", it.artifactId)
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


}