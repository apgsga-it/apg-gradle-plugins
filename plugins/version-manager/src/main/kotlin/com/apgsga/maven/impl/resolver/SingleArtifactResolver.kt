package com.apgsga.maven.impl.resolver

import org.apache.maven.model.Dependency

class SingleArtifactResolver(private var updateArtifact: String) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<Dependency>? {
        logger.info(" Updateing with single Artifact: $updateArtifact")
        val split = updateArtifact.split(":")
        assert(split.size == 3) { "Implausible maven coordinates: $updateArtifact" }
        val dep = Dependency()
        dep.groupId = split[0]
        dep.artifactId = split[1]
        dep.version = split[2]
        logger.info("Adding $dep" )
        return listOf(dep)
    }


}


