package com.apgsga.maven.impl.resolver

import com.apgsga.maven.LoggerDelegate
import com.apgsga.maven.MavenArtifact
import com.apgsga.maven.VersionResolver

 class MavenArtifactListResolver(var mavenArtifacts: Collection<MavenArtifact>? = ArrayList()) : VersionResolver {
     protected val logger by LoggerDelegate()

     override fun getVersion(groupId: String, artifactId: String): String {
         logger.info("Resolving Version for groupId <${groupId}> , artifactId <${artifactId}< with ${this}")
         mavenArtifacts?.forEach{
             logger.info("Resolving with $it")
             if (it.artifactid == artifactId && it.groupId == groupId) return it.version
             logger.info("Not resolved ")
         }
         logger.info("Could'nt resolve Version for groupId <${groupId}> , artifactId <${artifactId}< with ${this}")
         return ""
     }
 }