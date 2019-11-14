package com.apgsga.maven

data class MavenArtifact(val groupId: String, val artifactid: String, val version: String, val type: String)

interface MavenBomManager {

    fun loadModel(groupId: String, artifactid: String, version: String): Collection<MavenArtifact>

    fun loadModel(repoPathBom: String): Collection<MavenArtifact>

}

