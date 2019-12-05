package com.apgsga.maven.impl.resolver

import com.apgsga.maven.MavenArtifact
import com.apgsga.maven.MavenBomManager

class BomManagerArtifactVersionResolver(private var bomArtifact: String, private var recursive: Boolean? = true, private var mavenBomManager: MavenBomManager) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<MavenArtifact>? {
       return mavenBomManager.retrieve(bomArtifact, recursive?:true)
    }


}
