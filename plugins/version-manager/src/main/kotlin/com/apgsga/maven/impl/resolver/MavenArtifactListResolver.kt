package com.apgsga.maven.impl.resolver

import com.apgsga.maven.LoggerDelegate
import com.apgsga.maven.VersionResolver
import org.apache.maven.model.Dependency

class MavenArtifactListResolver(var mavenArtifacts: Collection<Dependency>? = ArrayList()) : VersionResolver {
     protected val logger by LoggerDelegate()

     override fun getVersion(groupId: String, artifactId: String): String {
         logger.debug("Resolving Version for groupId <${groupId}> , artifactId <${artifactId}< with ${this}")
         mavenArtifacts?.forEach{
             logger.debug("Resolving with $it")
             if (it.artifactId == artifactId && it.groupId == groupId) return it.version
             logger.debug("Not resolved ")
         }
         logger.debug("Could'nt resolve Version for groupId <${groupId}> , artifactId <${artifactId}< with ${this}")
         return ""
     }

     override fun getMavenArtifactList(): Collection<Dependency>? {
         return mavenArtifacts
     }

 }