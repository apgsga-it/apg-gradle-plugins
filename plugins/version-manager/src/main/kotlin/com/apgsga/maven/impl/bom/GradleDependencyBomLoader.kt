package com.apgsga.maven.impl.bom

import com.apgsga.maven.BomLoader
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class GradleDependencyBomLoader(val project: Project) : BomLoader {
    override fun load(bomGroupId: String, bomArtifactid: String, bomVersion: String): InputStream {
        val mavenCoordinates = "${bomGroupId}:${bomArtifactid}:${bomVersion}"
        return load(mavenCoordinates)
    }

    override fun load(mavenCoordinates: String): InputStream {
        val dependency = project.dependencies.create("${mavenCoordinates}@pom")
        val configuration = project.configurations.detachedConfiguration(dependency)
        val file: File = configuration.files.iterator().next()
        return FileInputStream(file)
    }
}