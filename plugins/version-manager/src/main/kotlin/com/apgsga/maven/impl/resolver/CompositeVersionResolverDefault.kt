package com.apgsga.maven.impl.resolver

import com.apgsga.maven.CompositeVersionResolver
import com.apgsga.maven.MavenArtifact
import com.apgsga.maven.VersionResolver
import org.apache.maven.model.Dependency

class CompositeVersionResolverDefault : CompositeVersionResolver{

    private val versionResolvers = mutableListOf<Pair<Int, VersionResolver>>()

    override fun add(order: Int, versionResolver: VersionResolver) {
        versionResolvers.add(Pair(order,versionResolver))
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

    override fun getMavenArtifactList(): Collection<Dependency>? {
        val artifactMap = mutableMapOf<String,Dependency>()
        val sortResolvers = versionResolvers.sortedByDescending { it.first}
        sortResolvers.forEach {
            it.second.getMavenArtifactList()?.forEach {
                artifactMap["${it.artifactId}:${it.groupId}"] = it
            }
        }
        return artifactMap.values
    }
}