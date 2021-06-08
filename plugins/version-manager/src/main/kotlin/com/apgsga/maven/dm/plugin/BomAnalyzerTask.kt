package com.apgsga.maven.dm.plugin

import com.apgsga.maven.impl.bom.GradleDependencyBomLoader
import com.apgsga.maven.impl.bom.MavenBomManagerDefault
import org.apache.maven.model.Dependency
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import kotlin.time.measureTimedValue

abstract class BomAnalyzerTask : DefaultTask() {


    @get:Input
    abstract val bomCoordinates: ListProperty<String>

    @get:Input
    abstract val recursive: Property<Boolean>

    init {
        bomCoordinates.convention(listOf("com.affichage.common.maven:dm-bom:9.1.0.SSO-SNAPSHOT"))
        recursive.convention(false)
    }

    @TaskAction
    fun analyzeBoms() {
        println("Analyzing the following Boms: ")
        val dependenciesMap = mutableMapOf<String, Collection<Dependency>>()
        val mavenBomManagerDefault = MavenBomManagerDefault(GradleDependencyBomLoader(project))
        bomCoordinates.get().forEach {
            println("Retrieving Dependencies for Bom Maven Coordinates: $it")
            val dependencies = mavenBomManagerDefault.retrieve(it, recursive.get())
            dependenciesMap[it] = dependencies
        }
        println("Dumping all dependencies")
        // TODO (che, 8.6) : Report Intersection of all Dependency Management Collections
        // TODO (che, 8.6) : Report redundancy
        // TODO (che, 8.6) : Report Version conflicts
        dependenciesMap.forEach{(key, dependencies) ->
            println ("***** All Bom: $key Dependencies in Dependency Management: ")
            dependencies.forEach {
                println("Bom : $key, has: ${it.toString()}")
            }
        }
        println("Done.")
    }
}