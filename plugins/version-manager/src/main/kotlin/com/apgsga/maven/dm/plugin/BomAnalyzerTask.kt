package com.apgsga.maven.dm.plugin

import com.apgsga.maven.impl.bom.GradleDependencyDependencyLoader
import com.apgsga.maven.impl.bom.MavenBomManagerDefault
import org.apache.maven.model.Dependency
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class BomAnalyzerTask : DefaultTask() {


    @get:Input
    abstract val bomCoordinates: SetProperty<String>

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
        val mavenBomManagerDefault = MavenBomManagerDefault(GradleDependencyDependencyLoader(project))
        bomCoordinates.get().forEach {
            println("Retrieving Dependencies for Bom Maven Coordinates: $it")
            val dependencies = mavenBomManagerDefault.retrieve(it, recursive.get())
            bomDependenciesMap[it] = dependencies
        }
        // TODO (che, 8.6) : Report Version conflicts
        val depencenciesInBomsMap = mutableMapOf<String, MutableSet<String>>()
        val depencenciesVersionsMap = mutableMapOf<String, MutableSet<String>>()
        bomDependenciesMap.forEach{ (key, dependencies) ->
            dependencies.forEach {
                val mavenCoordinate = "${it.groupId}:${it.artifactId}"
                if (depencenciesInBomsMap.containsKey(mavenCoordinate)){
                    depencenciesInBomsMap.getValue(mavenCoordinate).add(key)
                    depencenciesVersionsMap.getValue(mavenCoordinate).add(it.version)
                } else {
                    depencenciesInBomsMap[mavenCoordinate] = mutableSetOf(key)
                    depencenciesVersionsMap[mavenCoordinate] = mutableSetOf(it.version)
                }
            }
        }
        println("Artifact Redundancies:")
        depencenciesInBomsMap.forEach { (dependency, bomCoordinates) ->
            if (bomCoordinates.size > 1) {
                println("Dependency : <$dependency> in multiple boms : ${bomCoordinates.toString()}")
                if (depencenciesVersionsMap[dependency]!!.size > 1 ) {
                    println ("!!!! with different Versions${depencenciesVersionsMap[dependency].toString()}")
                }
            }
        }
        println("Done.")
    }
}