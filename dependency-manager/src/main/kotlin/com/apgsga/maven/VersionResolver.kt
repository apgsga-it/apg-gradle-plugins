package com.apgsga.maven

interface VersionResolver {
    
    fun getVersion(groupId: String , artifactId: String) : String
}