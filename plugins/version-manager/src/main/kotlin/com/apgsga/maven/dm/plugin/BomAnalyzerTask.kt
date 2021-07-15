package com.apgsga.maven.dm.plugin

import com.apgsga.maven.impl.bom.GradleDependencyLoader
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
        bomCoordinates.convention(listOf(""))
        recursive.convention(false)
    }

    @TaskAction
    fun analyzeBoms() {
        println("Analyzing the following Boms: ")
        val bomDependenciesMap = mutableMapOf<String, Collection<Dependency>>()
        val mavenBomManagerDefault = MavenBomManagerDefault(GradleDependencyLoader(project))
        bomCoordinates.get().forEach {
            println("Retrieving Dependencies for Bom Maven Coordinates: $it")
            val dependencies = mavenBomManagerDefault.retrieve(it, recursive.get())
            bomDependenciesMap[it] = dependencies
        }
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
        println("******* The following dependencies are redundant in the analyzed Boms: ")
        depencenciesInBomsMap.forEach { (dependency, bomCoordinates) ->
            if (bomCoordinates.size > 1) {
                println("Dependency : <$dependency> in multiple in following boms : $bomCoordinates")
                if (depencenciesVersionsMap[dependency]!!.size > 1 ) {
                    print (" with version conflicts : ${depencenciesVersionsMap[dependency].toString()}\n")
                }
            }
        }

        bomCoordinates.get().forEach { itCompared ->
            val others = bomCoordinates.get().toMutableSet()
            others.remove(itCompared)
            val dependenciesCompared = bomDependenciesMap[itCompared]
            others.forEach {
                val dependencies = bomDependenciesMap[it]
                println("******* The following dependencies in the bom <$itCompared> are not comtained in bom  <$it> :  ")
                val depNotContained = dependenciesCompared!!.minus(dependencies)
                depNotContained?.forEach { dep -> println("$dep In  <$itCompared> not in <$it>" ) }
            }
        }
        println("Done.")
    }
}