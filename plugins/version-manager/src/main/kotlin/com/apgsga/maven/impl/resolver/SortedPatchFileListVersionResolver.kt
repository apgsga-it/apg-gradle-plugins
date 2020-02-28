package com.apgsga.maven.impl.resolver

import com.apgsga.microservice.patch.api.Patch
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.maven.model.Dependency
import java.io.File

class SortedPatchFileListVersionResolver(var patchFiles: Collection<File>, var patchComparator: PatchComparator = PatchComparator.PATCHNUMBER_ASC) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<Dependency>? {
        val mapper = ObjectMapper()
        val patchList: MutableList<Patch> = mutableListOf()
        patchFiles.forEach {
            patchList.add(mapper.readValue(it, Patch::class.java))
        }
        val patchCompEnum = this.patchComparator
        val sortedList = patchList.sortedWith(patchCompEnum.patchComparator)
        val dependencies = mutableListOf<Dependency>()
        sortedList.forEach { patch ->
            patch.mavenArtifacts.forEach {
                val dependency = Dependency()
                dependency.artifactId = it.artifactId
                dependency.groupId = it.groupId
                dependency.version = it.version
                dependency.type = "jar"
                dependencies.add(dependency)
            }
        }
        return dependencies
    }

    // TODO (jhe,uge, che, 10.12): determine which Sort Criteria are necessary , idee: should be contained in Patch
    enum class PatchComparator(val patchComparator: Comparator<Patch>) {
        PATCHNUMBER_ASC(compareBy<Patch> { it.patchNummer }),
        PATCHNUMBER_DESC(compareByDescending { it.patchNummer }),
        TAGNR_ASC(compareBy<Patch> { it.tagNr }),
        TAGNR_DESC(compareByDescending<Patch> { it.tagNr })
    }

}


