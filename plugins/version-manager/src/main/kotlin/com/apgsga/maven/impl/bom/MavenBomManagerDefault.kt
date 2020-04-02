package com.apgsga.maven.impl.bom

import com.apgsga.maven.BomLoader
import com.apgsga.maven.LoggerDelegate
import com.apgsga.maven.MavenBomManager
import com.apgsga.maven.dm.ext.version
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import java.io.InputStream

/**
 *  Default implementation
 *  @author che
 *
 */
class MavenBomManagerDefault(private val bomLoader: BomLoader) : MavenBomManager {

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
        logger.debug("Loading Maven Model")
        val mavenReader = MavenXpp3Reader()
        val mavenModel = mavenReader.read(bomFile)
        val dependencies = mavenModel.dependencyManagement.dependencies
        val artifactList = list(dependencies, mavenModel, artList, resursive)
        logger.debug("Loading Maven Model done.")
        logger.debug("Resolved the following artifacts from pom $mavenModel.groupId, ${mavenModel.artifactId} : $artifactList")
        return artifactList
    }

    private fun list(dependencies: MutableList<Dependency>, mavenModel: Model, artList: Collection<Dependency>, recursive: Boolean): Collection<Dependency> {
        var artifactList = artList
        for (dependency in dependencies) {
            val resolvedVersion = resolveVersion(mavenModel, dependency.version)
            if (resolvedVersion != null && dependency.type == "pom") {
                if (recursive) {
                    // recursively resolve bom
                    val stream = bomLoader.load(dependency.groupId, dependency.artifactId, dependency.version)
                    artifactList = loadModel(stream, artifactList, true)
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


    override fun retrieve(bomArtifact: String, recursive: Boolean): Collection<Dependency> {
        val artifactList = emptyList<Dependency>()
        val stream = bomLoader.load(bomArtifact)
        return loadModel(stream, artifactList, recursive)
    }

    override fun retrieve(bomGroupId: String, bomArtifactid: String, bomVersion: String, recursive: Boolean): Collection<Dependency> {
        val artifactList = emptyList<Dependency>()
        val stream = bomLoader.load(bomGroupId,bomArtifactid, bomVersion)
        return loadModel(stream,artifactList,recursive)
    }


}