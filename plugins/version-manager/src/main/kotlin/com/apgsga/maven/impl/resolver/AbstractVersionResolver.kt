package com.apgsga.maven.impl.resolver

import com.apgsga.maven.LoggerDelegate
import com.apgsga.maven.MavenArtifact
import com.apgsga.maven.VersionResolver

abstract class AbstractVersionResolver : VersionResolver {

    protected val logger by LoggerDelegate()
    private val recommender = MavenArtifactListResolver()

    override fun getVersion(groupId: String, artifactId: String): String {
        logger.info("Resolving Version for groupId <${groupId}> , artifactId <${artifactId}< with ${this}")
        recommender.mavenArtifacts = getMavenArtifactList()
        logger.info("Got Artifact List: ${recommender.mavenArtifacts.toString()} ")
        return recommender.getVersion(groupId, artifactId)
    }

    abstract fun getMavenArtifactList() : Collection<MavenArtifact>?
}