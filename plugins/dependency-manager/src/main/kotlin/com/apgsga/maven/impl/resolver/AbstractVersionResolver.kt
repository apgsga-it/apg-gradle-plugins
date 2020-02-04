package com.apgsga.maven.impl.resolver

import com.apgsga.maven.LoggerDelegate
import com.apgsga.maven.MavenArtifact
import com.apgsga.maven.VersionResolver

abstract class AbstractVersionResolver : VersionResolver {

    protected val logger by LoggerDelegate()
    private val recommender = MavenArtifactListResolver()

    override fun getVersion(groupId: String, artifactId: String): String {
        recommender.mavenArtifacts = getMavenArtifactList()
        return recommender.getVersion(groupId, artifactId)
    }

    abstract fun getMavenArtifactList() : Collection<MavenArtifact>?
}