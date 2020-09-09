package com.apgsga.maven.impl.resolver

import org.apache.maven.model.Dependency

class SingleArtifactResolver(private var updateArtifact: String) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<Dependency>? {
        logger.debug(toString())
        val split = updateArtifact.split(":")
        assert(split.size == 3) { "Implausible maven coordinates: $updateArtifact" }
        val dep = Dependency()
        dep.groupId = split[0]
        dep.artifactId = split[1]
        dep.version = split[2]
        return listOf(dep)
    }


}


