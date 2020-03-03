package com.apgsga.maven.impl.resolver

import com.apgsga.maven.MavenArtifact
import com.apgsga.maven.MavenBomManager
import org.apache.maven.model.Dependency

class BomVersionResolver(private var bomArtifact: String, private var recursive: Boolean? = true, private var mavenBomManager: MavenBomManager) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<Dependency>? {
        val retrieved = mavenBomManager.retrieve(bomArtifact, recursive ?: true)
        logger.info("Got versions: $retrieved")
        return retrieved
    }
}
