package com.apgsga.maven

import com.apgsga.gradle.repository.Repository
import com.apgsga.gradle.repository.RepositoryBuilderFactory
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.InputStream

/**
 *  Default implementation
 *  @author che
 *
 */
class MavenBomManagerDefaultImpl(repoPathBom: String, repoName: String, repoUser: String?, repoPass: String?) : MavenBomManager {

    private val logger by LoggerDelegate()
    private val repository: Repository = RepositoryBuilderFactory.createFor(repoPathBom, repoName, repoUser, repoPass).build()


    private fun resolveVersion(mavenModel: Model, version: String): String? {
        return if (version.startsWith("\${") && version.endsWith("}")) {
            val versionProperty = version.substring(2, version.length - 1)
            mavenModel.properties.getProperty(versionProperty)
        } else
            version
    }


    private fun loadModel(bomFile: InputStream, artList: Collection<MavenArtifact>): Collection<MavenArtifact> {
        logger.lifecycle("Loading Maven Model")
        val mavenReader = MavenXpp3Reader()
        val mavenModel = mavenReader.read(bomFile)
        val dependencies = mavenModel.dependencyManagement.dependencies
        val artifactList = list(dependencies, mavenModel, artList)
        logger.lifecycle("Loading Maven Model done.")
        logger.debug("Resolved the following artifactfs: $artifactList")
        return artifactList
    }

    private fun list(dependencies: MutableList<Dependency>, mavenModel: Model, artList: Collection <MavenArtifact>) : Collection<MavenArtifact> {
        var artifactList = artList
        for (dependency in dependencies) {
            val resolvedVersion = resolveVersion(mavenModel, dependency.version)
            if (resolvedVersion != null && dependency.type == "pom") {
                // recursively resolve bom
                artifactList = loadModel(buildPath(dependency.groupId, dependency.artifactId, dependency.version), artifactList)
            } else if (resolvedVersion != null) {
                artifactList += MavenArtifact(dependency.groupId, dependency.artifactId, resolvedVersion, dependency.type)
                } else {
                    logger.error("No artifact found for groupid:  $dependency.groupId, artifactid: $dependency.artifactId, version: $resolvedVersion")
                }

        }
        return artifactList
    }

    private fun buildPath(groupId: String, artifactid: String, version: String): String {
        return "$groupId/$artifactid/$version/$artifactid-$version.pom.xml"
    }

    private fun loadModel(repoPathBom: String, artList: Collection<MavenArtifact>): Collection<MavenArtifact> {
        val stream = repository.download(repoPathBom)
        return loadModel(stream, artList)
    }

    override fun loadModel(repoPathBom: String): Collection<MavenArtifact> {
        val artifactList = emptyList<MavenArtifact>()
        return loadModel(repoPathBom, artifactList)
    }


    override fun loadModel(groupId: String, artifactid: String, version: String): Collection<MavenArtifact> {
        return loadModel(buildPath(groupId, artifactid, version))
    }
}