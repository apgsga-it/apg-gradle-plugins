package com.apgsga.maven.dm.plugin

import com.apgsga.maven.impl.bom.GradleDependencyBomLoader
import com.apgsga.maven.impl.bom.MavenBomManagerDefault
import org.apache.maven.model.Dependency
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

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
        val bomDependenciesMap = mutableMapOf<String, Collection<Dependency>>()
        val mavenBomManagerDefault = MavenBomManagerDefault(GradleDependencyBomLoader(project))
        bomCoordinates.get().forEach {
            println("Retrieving Dependencies for Bom Maven Coordinates: $it")
            val dependencies = mavenBomManagerDefault.retrieve(it, recursive.get())
            bomDependenciesMap[it] = dependencies
        }
        // TODO (che, 8.6) : Report Version conflicts
        val depencenciesInBomsMap = mutableMapOf<String, MutableCollection<String>>()
        bomDependenciesMap.forEach{ (key, dependencies) ->
            println ("***** All Bom: $key Dependencies in Dependency Management: ")
            dependencies.forEach {
                println("Bom : $key, has: ${it.toString()}")
                val bomCoordinate = "${it.groupId}:${it.artifactId}:${it.version}"
                if (depencenciesInBomsMap.containsKey(bomCoordinate)){
                    depencenciesInBomsMap.getValue(bomCoordinate).add(key)
                } else {
                    depencenciesInBomsMap.put(bomCoordinate, mutableListOf(key))
                }
            }
        }
        println{"Report Redundancies:"}
        depencenciesInBomsMap.forEach { (dependency, bomCoordinates) ->
            if (bomCoordinates.size > 1) {
                println("Dependency : <$dependency> in multiple boms : ${bomCoordinates.toString()}  ")
            }
        }
        println("Done.")
    }
}