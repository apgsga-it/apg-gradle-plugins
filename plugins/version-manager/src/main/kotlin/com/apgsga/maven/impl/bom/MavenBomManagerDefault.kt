package com.apgsga.maven.impl.bom

import com.apgsga.gradle.repository.Repository
import com.apgsga.maven.LoggerDelegate
import com.apgsga.maven.MavenBomManager
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.InputStream
import java.util.*
import java.util.function.Predicate
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
     * Management Section into a List of [Dependency]
     *
     * @param bomFile Inputstream of a pom.xml File3
     * @param artList the List, in which the Maven Artifacts are collected.
     * @return Collection of [Dependency]
     */
    private fun loadModel(bomFile: InputStream, artList: Collection<Dependency>, resursive: Boolean): Collection<Dependency> {
        logger.info("Loading Maven Model")
        val mavenReader = MavenXpp3Reader()
        val mavenModel = mavenReader.read(bomFile)
        val dependencies = mavenModel.dependencyManagement.dependencies
        val artifactList = list(dependencies, mavenModel, artList, resursive)
        logger.info("Loading Maven Model done.")
        logger.info("Resolved the following artifacts from pom $mavenModel.groupId, ${mavenModel.artifactId} : $artifactList")
        return artifactList
    }

    private fun list(dependencies: MutableList<Dependency>, mavenModel: Model, artList: Collection<Dependency>, recursive: Boolean): Collection<Dependency> {
        var artifactList = artList
        for (dependency in dependencies) {
            val resolvedVersion = resolveVersion(mavenModel, dependency.version)
            if (resolvedVersion != null && dependency.type == "pom") {
                if (recursive) {
                    // recursively resolve bom
                    artifactList = loadModelFromPath(buildPath(dependency.groupId, dependency.artifactId, dependency.version), artifactList, recursive = true)
                }
            } else if (resolvedVersion != null) {
                val copy =  dependency.clone()
                copy.version = resolvedVersion
                artifactList += copy
            } else {
                logger.error("No Version found for groupid:  $dependency.groupId, artifactid: $dependency.artifactId, version: $resolvedVersion")
            }

        }
        return artifactList
    }

    private fun buildPath(groupId: String, artifactid: String, version: String): String {
        // TODO (che, 27.2 ) : make this platform independent, necessary?
        val groupPath = groupId.replace(".","/")
        return "$groupPath/$artifactid/$version/$artifactid-$version.pom"
    }

    private fun loadModelFromPath(repoPathBom: String, artList: Collection<Dependency>, recursive: Boolean): Collection<Dependency> {
        logger.info("Loading Model from RepositoryPath: ${repoPathBom}")
        val stream = repository.download(repoPathBom)
        return loadModel(stream, artList, recursive)
    }

    override fun retrieve(bomArtifact: String, recursive: Boolean): Collection<Dependency> {
        val artifactList = emptyList<Dependency>()
        val (groupId, artifactId, version) = bomArtifact.split(':')
        return loadModelFromPath(buildPath(groupId, artifactId, version), artifactList, recursive)
    }

    override fun retrieve(bomGroupId: String, bomArtifactid: String, bomVersion: String, recursive: Boolean): Collection<Dependency> {
        return retrieve("$bomGroupId:$bomArtifactid:$bomVersion", recursive)
    }

    override fun intersect(firstBomArtifact: String, secondBomArtifact: String, recursive: Boolean): Collection<Dependency> {
        val firstDependencyList = retrieve(firstBomArtifact, recursive)
        val secDepList = retrieve(secondBomArtifact, recursive)
        return firstDependencyList.stream().filter(Predicate {
            secDepList.forEach{secIt ->
                if (it.artifactId == secIt.artifactId && it.groupId == secIt.groupId) {
                    return@Predicate true
                }
            }
            return@Predicate false
        }).collect(Collectors.toList())
    }

    override fun retrieveAsProperties(bomArtifact: String, recursive: Boolean): Properties {
        val result = retrieve(bomArtifact, recursive)
        val props = Properties()
        result.forEach {
            val key = it.groupId + ":" + it.artifactId
            props[key] = it.version
        }
        return props
    }





}