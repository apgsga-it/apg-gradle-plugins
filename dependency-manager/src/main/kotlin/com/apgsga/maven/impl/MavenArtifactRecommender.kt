package com.apgsga.maven.impl

import com.apgsga.maven.DependencyRecommender
import com.apgsga.maven.LoggerDelegate
import com.apgsga.maven.MavenBomManager

class MavenArtifactRecommender(private var bomArtifact: String, private var recursive: Boolean? = true, private var mavenBomManager: MavenBomManager) : DependencyRecommender {

    private val logger by LoggerDelegate()

    override fun getVersion(groupId: String, artifactId: String): String {
        val result = mavenBomManager.retrieve(bomArtifact, recursive?:true)
        result.forEach{
            if (it.artifactid == artifactId && it.groupId == groupId) return it.version
        }
        return ""
    }


}
