package com.apgsga.maven.impl.resolver

import com.apgsga.maven.MavenBomManager
import com.apgsga.maven.VersionResolver

class BomListVersionResolver(private var recursive: Boolean? = true, private var mavenBomManager: MavenBomManager) : VersionResolver {

    private val versionResolvers = mutableListOf<Pair<Int, BomVersionResolver>>()

    fun add(order: Int, bomArtifact: String) {
        versionResolvers.add(Pair(order, BomVersionResolver(bomArtifact,recursive,mavenBomManager)))
    }

    override fun getVersion(groupId: String, artifactId: String): String {
        val sortResolvers = versionResolvers.sortedBy { it.first}
        sortResolvers.forEach {
           val result =  it.second.getVersion(groupId, artifactId)
            if (result.isNotEmpty())  {
                return result
            }
        }
        return ""
    }
}