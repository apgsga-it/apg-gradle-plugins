package com.apgsga.maven.impl.bom

import com.apgsga.gradle.repository.Repository
import com.apgsga.maven.LoggerDelegate
import com.apgsga.maven.MavenArtifact
import com.apgsga.maven.MavenBomManager
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.InputStream
import java.util.*
import java.util.stream.Collectors

/**
 *  Default implementation
 *  @author che
 *
 */
class MavenBomManagerDefault(private val repository: Repository) : MavenBomManager {

    private val logger by LoggerDelegate()

    private fun resolveVersion(mavenModel: Model, version: String): String? {
        return if (version.startsWith("\${") && version.endsWith("}")) {
            val versionProperty = version.substring(2, version.length - 1)
            mavenModel.properties.getProperty(versionProperty)
        } else
            version
    }

    /**
     * Loads the Maven [Model] from a pom.xml InputStream and extracts the Dependencies of the Dependency
     * Management Section into a List of [MavenArtifact]
     *
     * @param bomFile Inputstream of a pom.xml File3
     * @param artList the List, in which the Maven Artifacts are collected.
     * @return Collection of [MavenArtifact]
     */
    private fun loadModel(bomFile: InputStream, artList: Collection<MavenArtifact>, resursive: Boolean): Collection<MavenArtifact> {
        logger.info("Loading Maven Model")
        val mavenReader = MavenXpp3Reader()
        val mavenModel = mavenReader.read(bomFile)
        val dependencies = mavenModel.dependencyManagement.dependencies
        val artifactList = list(dependencies, mavenModel, artList, resursive)
        logger.info("Loading Maven Model done.")
        logger.debug("Resolved the following artifacts from pom $mavenModel.groupId, ${mavenModel.artifactId} : $artifactList")
        return artifactList
    }

    private fun list(dependencies: MutableList<Dependency>, mavenModel: Model, artList: Collection<MavenArtifact>, recursive: Boolean): Collection<MavenArtifact> {
        var artifactList = artList
        for (dependency in dependencies) {
            val resolvedVersion = resolveVersion(mavenModel, dependency.version)
            if (resolvedVersion != null && dependency.type == "pom") {
                if (recursive) {
                    // recursively resolve bom
                    artifactList = loadModelFromPath(buildPath(dependency.groupId, dependency.artifactId, dependency.version), artifactList, recursive = true)
                }
            } else if (resolvedVersion != null) {
                artifactList += MavenArtifact(dependency.groupId, dependency.artifactId, resolvedVersion, dependency.type)
            } else {
                logger.error("No Version found for groupid:  $dependency.groupId, artifactid: $dependency.artifactId, version: $resolvedVersion")
            }

        }
        return artifactList
    }

    private fun buildPath(groupId: String, artifactid: String, version: String): String {
        return "$groupId/$artifactid/$version/$artifactid-$version.pom"
    }

    private fun loadModelFromPath(repoPathBom: String, artList: Collection<MavenArtifact>, recursive: Boolean): Collection<MavenArtifact> {
        val stream = repository.download(repoPathBom)
        return loadModel(stream, artList, recursive)
    }

    override fun retrieve(bomArtifact: String, recursive: Boolean): Collection<MavenArtifact> {
        val artifactList = emptyList<MavenArtifact>()
        val (groupId, artifactId, version) = bomArtifact.split(':')
        return loadModelFromPath(buildPath(groupId, artifactId, version), artifactList, recursive)
    }

    override fun retrieve(bomGroupId: String, bomArtifactid: String, bomVersion: String, recursive: Boolean): Collection<MavenArtifact> {
        return retrieve("$bomGroupId:$bomArtifactid:$bomVersion", recursive)
    }

    override fun intersect(firstBomArtifact: String, secondBomArtifact: String, recursive: Boolean): Collection<MavenArtifact> {
        val firstDependencyList = retrieve(firstBomArtifact, recursive)
        val secDepList = retrieve(secondBomArtifact, recursive)
        return firstDependencyList.stream().filter(secDepList::contains).collect(Collectors.toList())
    }

    override fun retrieveAsProperties(bomArtifact: String, recursive: Boolean): Properties {
        val result = retrieve(bomArtifact, recursive)
        val props = Properties()
        result.forEach {
            val key = it.groupId + ":" + it.artifactid
            props[key] = it.version
        }
        return props
    }



}