package com.apgsga.maven.impl.resolver

import com.apgsga.maven.MavenArtifact
import com.apgsga.microservice.patch.api.Patch
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class SortedPatchFileListArtifactVersionResolver(var patchFiles: Collection<File>, var ascending: Boolean? = true) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<MavenArtifact>? {
        val mapper = ObjectMapper()
        val patchList : MutableList<Patch> = mutableListOf()
        patchFiles.forEach{
            patchList.add(mapper.readValue(it, Patch::class.java))
        }
        // TODO (che, 5.12) : determine precise needed sort criterias and if they need to be configurable
        val sortedList = if (this.ascending!!) {
            patchList.sortedBy { it.patchNummer }
        } else {
            patchList.sortedByDescending { it.patchNummer }
        }
        return sortedList.flatMap { itouter -> itouter.mavenArtifacts.map {MavenArtifact(it.groupId,it.artifactId,it.version,"jar")} }
    }

}