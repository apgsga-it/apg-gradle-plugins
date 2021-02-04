package com.apgsga.maven.dm.ext


import com.apgsga.maven.VersionResolver
import com.apgsga.maven.impl.resolver.BomVersionGradleResolverBuilder
import com.apgsga.maven.impl.resolver.CompositeVersionResolverBuilder
import com.apgsga.maven.impl.resolver.PatchFileVersionResolverBuilder
import com.apgsga.maven.impl.resolver.SingleArtifactResolverBuilder
import com.apgsga.revision.manager.domain.RevisionManager
import com.apgsga.revision.manager.domain.RevisionManagerBuilder
import groovy.lang.Closure
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
    throw IllegalArgumentException("Illegal Version for Artifact with baseVersion: $baseVersion and revision: $revision")

}

open class VersionResolutionExtension(val project: Project, private val revisionManagerBuilder: RevisionManagerBuilder) {
    var configurationName: String = "null"
    var installTarget: String = "null"
    var serviceName: String = "null"
    var bomArtifactId: String? = null
    var bomGroupId: String? = null
    var bomBaseVersion: String? = null
    var patchFilePath: String? = null
    var updateArtifact: String? = null
    var algorithm: RevisionManagerBuilder.AlgorithmTyp = RevisionManagerBuilder.AlgorithmTyp.SNAPSHOT
    var newRevision: Boolean = false
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
    val revisionManger: RevisionManager
        get() {
            if (_revisionManger == null) {
                _revisionManger = revisionManagerBuilder.revisionRootPath(revisionRootPath
                        ?: project.gradle.gradleUserHomeDir.absolutePath).cloneTargetPath(cloneTargetPath).algorithm(algorithm).build()
            }
            return _revisionManger as RevisionManager
        }

    private var _patchRevisionManager: RevisionManager? = null
    val patchRevisionManager: RevisionManager
        get() {
            if(_patchRevisionManager == null) {
                _patchRevisionManager = revisionManagerBuilder.revisionRootPath(patchRevisionRootPath).algorithm(algorithm).build()
            }
            return _patchRevisionManager as RevisionManager
        }

    var revisionRootPath: String? = null
    var cloneTargetPath: String? = null
    var patchRevisionRootPath: String? = null

    private var _lastRevision: String? = null
    private val lastRevision: String
        get() {
            project.logger.info("lastRevision is getting retrieved. Current _lastevision=$_lastRevision")
            _lastRevision = revisionManger.lastRevision(serviceName, installTarget)
            project.logger.info("Got Lastrevision: $_revision")
            return _lastRevision as String
        }
    private var _revision: String? = null
    private val revision: String
        get() {

            project.logger.info("Revision is getting retrieved, newRevision=$newRevision , _revision=$_revision")

            if (_revision == null) {
                if(newRevision) {
                    project.logger.info("Before setting _revision -> we first save")
                    save()
                    project.logger.info("Setting _revision from patchRevisionMananger")
                    _revision = patchRevisionManager.lastRevision(serviceName, installTarget)
                }
                else {
                    project.logger.info("Setting _revision from revisionManger")
                    _revision = revisionManger.lastRevision(serviceName, installTarget)
                }
                project.logger.info("Calculation Revision: $_revision and Lastrevision:  $_lastRevision")
            }
            project.logger.info("Got Revision: $_revision")
            return _revision as String

        }


    fun save() {
        if (!newRevision) return
        val nextRev = revisionManger.nextRevision()
        project.logger.info("Saving a new revision for $serviceName on $installTarget : $nextRev")
        patchRevisionManager.saveRevision(serviceName,installTarget,nextRev,bomBaseVersion)
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
                return@tasks
            }
        }
    }

    private fun generateBomXml(publication: MavenPublication) {

        publication.artifactId = bomArtifactId
        publication.groupId = bomGroupId
        publication.version =  version(revision)
        project.logger.info("Publishing bom ${publication.artifactId}, ${publication.groupId} with version: ${publication.version}")
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
            }
        }
    }

    private fun buildVersionResolver(): VersionResolver {
        assert(bomArtifactId != null) { "bomArtifactId should not be null" }
        assert(bomGroupId != null) { "bomGroupId should not be null" }
        assert(bomBaseVersion != null) { "bomBaseVersion should not be null" }
        project.logger.info("Building VersionResolver with $bomArtifactId, $bomGroupId, $bomBaseVersion and Lastrevision:  $lastRevision with new Revision: $newRevision")
        configurationName.let { configureConfiguration(it) }
        val compositeResolverBuilder = CompositeVersionResolverBuilder()
        project.logger.info("Creating Dependency configuration")
        var cnt = 0
        // Patchfile has higher precedence
        updateArtifact?.let {
            compositeResolverBuilder.add(++cnt, SingleArtifactResolverBuilder(updateArtifact!!))
        }
        patchFilePath?.let {
            compositeResolverBuilder.add(++cnt,
                    PatchFileVersionResolverBuilder()
                            .patchFile(it))
        }
        compositeResolverBuilder.add(++cnt, BomVersionGradleResolverBuilder()
                .bomArtifact("${bomGroupId}:${bomArtifactId}:${version(lastRevision)}")
                .recursive(true))

        return compositeResolverBuilder.build(project)
    }

    fun version(revision: String): String {
        return version(bomBaseVersion, revision)
    }

    fun version(): String {
        return version(bomBaseVersion, revision)
    }


}