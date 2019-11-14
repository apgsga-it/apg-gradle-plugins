package com.apgsga.maven

import com.apgsga.gradle.repository.Repository
import com.apgsga.gradle.repository.RepositoryBuilderFactory
import java.io.InputStream

class  MavenBomManagerDefaultImpl(repoPathBom: String, repoName: String, repoUser: String, repoPass: String) : MavenBomManager {

    val repository : Repository = RepositoryBuilderFactory.createFor(repoPathBom, repoName, repoUser, repoPass).build()

    override fun loadModel(bomFile: InputStream) {

    }

    override fun loadModel(repoPathBom: String) {
    }

    override fun loadModel(groupId: String, artifactid: String, version: String) {
    }
}