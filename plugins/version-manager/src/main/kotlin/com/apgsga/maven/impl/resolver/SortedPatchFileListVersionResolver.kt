package com.apgsga.maven.impl.resolver

import com.apgsga.microservice.patch.api.Patch
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.maven.model.Dependency
import java.io.File

class SortedPatchFileListVersionResolver(var patchFiles: Collection<File>, var patchComparator: PatchComparator = PatchComparator.PATCHNUMBER_ASC) : AbstractVersionResolver() {

    override fun getMavenArtifactList(): Collection<Dependency>? {
        // TODO (jhe, che, 10.9) : Should we do more validation match Patchfile and current Build?
        val mapper = ObjectMapper()
        val patchList: MutableList<Patch> = mutableListOf()
        patchFiles.forEach {
            logger.info("Parsing Patchfile : ${it.absoluteFile} ")
            patchList.add(mapper.readValue(it, Patch::class.java))
            logger.info("Parsing Patchfile : ${it.absoluteFile}  done.")
        }
        val patchCompEnum = this.patchComparator
        val sortedList = patchList.sortedWith(patchCompEnum.patchComparator)
        val dependencies = mutableListOf<Dependency>()
        sortedList.forEach { patch ->
            patch.retrieveAllArtifactsToPatch().forEach {
                // Apg Patch Convention for Library Patches
                if (!it.version.endsWith("SNAPSHOT")) {
                    val dependency = Dependency()
                    dependency.artifactId = it.artifactId
                    dependency.groupId = it.groupId
                    dependency.version = it.version
                    dependency.type = "jar"
                    logger.info("Adding patch dependency $dependency" )
                    dependencies.add(dependency)
                }

            }
        }
        logger.info("Returning dependencies $dependencies")
        return dependencies
    }

    enum class PatchComparator(val patchComparator: Comparator<Patch>) {
        PATCHNUMBER_ASC(compareBy<Patch> { it.patchNumber }),
    }

}


