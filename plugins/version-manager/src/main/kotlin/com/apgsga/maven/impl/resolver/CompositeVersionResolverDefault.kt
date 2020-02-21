package com.apgsga.maven.impl.resolver

import com.apgsga.maven.CompositeVersionResolver
import com.apgsga.maven.VersionResolver

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
}