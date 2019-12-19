package com.apgsga.gradle.maven.dm.ext


import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project


open class VersionResolution(val name: String) {
    var order: Int = 0

}

class PomVersionResolution(name: String) : VersionResolution(name) {
    lateinit var artifact: String
    var recursive : Boolean =  false
    override fun toString(): String {
        return "PatchVersionResolution(name='$name', order='$order', artifact='$artifact', recursive='$recursive')"
    }
}


class PatchVersionResolution(name: String) : VersionResolution(name) {
    lateinit var parentDirName: String
    lateinit var fileName : String
    override fun toString(): String {
        return "PatchVersionResolution(name='$name', order='$order', parentDirName='$parentDirName', fileName='$fileName')"
    }

}



open class VersionResolutionExtension(val project: Project, val poms : NamedDomainObjectContainer<PomVersionResolution>, val patches : NamedDomainObjectContainer<PatchVersionResolution>) {
    var configurationName : String = "runtime"
    fun poms(closure: Closure<Any>) {
        poms.configure(closure)
    }

    fun patches(closure: Closure<Any>) {
        patches.configure(closure)
    }
    fun log() {
        poms.forEach { project.logger.info(it.toString())}
    }
}