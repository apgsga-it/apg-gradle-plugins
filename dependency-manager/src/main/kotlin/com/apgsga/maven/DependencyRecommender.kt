package com.apgsga.maven

interface DependencyRecommender {
    
    fun getVersion(groupId: String , artifactId: String) : String
}