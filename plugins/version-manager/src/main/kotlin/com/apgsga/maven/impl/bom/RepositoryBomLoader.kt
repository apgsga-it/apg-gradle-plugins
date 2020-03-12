package com.apgsga.maven.impl.bom

import com.apgsga.gradle.repository.Repository
import com.apgsga.maven.BomLoader
import com.apgsga.maven.LoggerDelegate
import java.io.InputStream

class RepositoryBomLoader (private val repository: Repository) : BomLoader {
    protected val logger by LoggerDelegate()
    override fun load(bomGroupId: String, bomArtifactid: String, bomVersion: String): InputStream {
        val buildPath = buildPath(bomGroupId, bomArtifactid, bomVersion)
        logger.info("Loading from $buildPath")
        return repository.download(buildPath)
    }
    override fun load(coordinates: String): InputStream {
        val (groupId, artifactId, version) = coordinates.split(":")
        return load(groupId,artifactId,version)
    }

    private fun buildPath(groupId: String, artifactid: String, version: String): String {
        // TODO (che, 27.2 ) : make this platform independent, necessary?
        val groupPath = groupId.replace(".", "/")
        return "$groupPath/$artifactid/$version/$artifactid-$version.pom"
    }


}