package com.apgsga.maven.impl.resolver

import com.apgsga.maven.MavenArtifact
import com.apgsga.microservice.patch.api.Patch
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class PatchFileArtifactVersionResolver(private var patchFile: File) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<MavenArtifact>? {
        val mapper = ObjectMapper()
        val patch = mapper.readValue(patchFile, Patch::class.java)
        return patch.mavenArtifacts.map { MavenArtifact(it.groupId,it.artifactId,it.version,"jar") }
    }

}