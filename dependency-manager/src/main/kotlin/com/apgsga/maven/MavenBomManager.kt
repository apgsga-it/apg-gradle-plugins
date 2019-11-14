package com.apgsga.maven

import java.io.InputStream

fun createMavenBomManager(repoUrlBom: String, repoName: String , repoUser: String , repoPass: String ) : MavenBomManager {
    return  MavenBomManagerDefaultImpl(repoUrlBom,repoName,repoUser,repoPass)
}

interface MavenBomManager {

    fun loadModel(groupId: String, artifactid: String, version: String)

    fun loadModel(repoPathBom: String)

    fun loadModel(bomFile: InputStream)
}

