package com.apgsga.maven.impl.resolver

import com.apgsga.maven.MavenArtifact
import com.apgsga.maven.VersionResolver

 class MavenArtifactListResolver(var mavenArtifacts: Collection<MavenArtifact>? = ArrayList()) : VersionResolver {

     override fun getVersion(groupId: String, artifactId: String): String {
         mavenArtifacts?.forEach{
             if (it.artifactid == artifactId && it.groupId == groupId) return it.version
         }
         return ""
     }
 }