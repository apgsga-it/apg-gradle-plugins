package com.apgsga.maven

import org.apache.maven.model.Dependency

interface VersionResolver {
    
    fun getVersion(groupId: String , artifactId: String) : String

    fun getMavenArtifactList() : Collection<Dependency>?
}