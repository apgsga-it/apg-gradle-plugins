package com.apgsga.maven.impl.resolver

import com.apgsga.maven.MavenArtifact
import com.apgsga.microservice.patch.api.Patch
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class SortedPatchFileListArtifactVersionResolver(var patchFiles: Collection<File>, var patchComparator: PatchComparator = PatchComparator.PATCHNUMBER_ASC) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<MavenArtifact>? {
        val mapper = ObjectMapper()
        val patchList: MutableList<Patch> = mutableListOf()
        patchFiles.forEach {
            patchList.add(mapper.readValue(it, Patch::class.java))
        }
        val patchCompEnum = this.patchComparator
        val sortedList = patchList.sortedWith(patchCompEnum.patchComparator)
        return sortedList.flatMap { outter -> outter.mavenArtifacts.map { MavenArtifact(it.groupId, it.artifactId, it.version, "jar") } }
    }

    enum class PatchComparator(val patchComparator: Comparator<Patch>) {
        PATCHNUMBER_ASC(compareBy<Patch> { it.patchNummer }),
        PATCHNUMBER_DESC(compareByDescending { it.patchNummer }),
        TAGNR_ASC(compareBy<Patch> { it.tagNr }),
        TAGNR_DESC(compareByDescending<Patch> { it.tagNr })
    }

}


