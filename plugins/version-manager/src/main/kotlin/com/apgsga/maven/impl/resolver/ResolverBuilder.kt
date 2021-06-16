package com.apgsga.maven.impl.resolver

import com.apgsga.maven.VersionResolver
import com.apgsga.maven.impl.bom.GradleDependencyDependencyLoader
import com.apgsga.maven.impl.bom.MavenBomManagerDefault
import org.apache.commons.lang.NotImplementedException
import org.gradle.api.Project
import java.io.File

// TODO (che, 11.12) The Builders will be verified against concrete usage scenarios

interface VersionResolverBuilder {
    fun build(): VersionResolver
    fun build(project: Project) : VersionResolver
}

// TODO (che,11.12) : Can this be done better?
fun <T : VersionResolverBuilder> create(clx: Class<T>): T = clx.newInstance()


data class BomVersionGradleResolverBuilder(
        var bomArtifact: String? = null,
        var recursive: Boolean? = false) : VersionResolverBuilder {

    fun bomArtifact(bomArtifact: String) = apply { this.bomArtifact = bomArtifact }
    fun recursive(recursive: Boolean?) = apply { this.recursive = recursive }

    override fun build(): VersionResolver {
        throw NotImplementedException()
    }

    override fun build(project: Project) : VersionResolver {
       return BomVersionResolver(bomArtifact!!, recursive, MavenBomManagerDefault(GradleDependencyDependencyLoader(project)))
    }

}

data class SingleArtifactResolverBuilder(
        var updateArtifact: String) : VersionResolverBuilder {

    override fun build(): VersionResolver {
        throw NotImplementedException()
    }

    override fun build(project: Project) : VersionResolver {
        return SingleArtifactResolver(updateArtifact)
    }

}

data class PatchFileVersionResolverBuilder( var patchFile: File? = null,var patchFileName: String? = null) : VersionResolverBuilder {

    fun patchFile(patchFile: String) = apply {
        require(this.patchFile == null) { "Either patchFile or patchFileName" }
        this.patchFileName = patchFile
    }
    override fun build(): VersionResolver {
        require(patchFileName != null) { "patchFile should'nt be null" }
        patchFile = if (patchFileName != null) {
            File(patchFileName)
        } else {
            throw IllegalArgumentException()
        }
        val patchFiles = mutableListOf<File>()
        patchFiles.add(patchFile!!)
        return SortedPatchFileListVersionResolver(patchFiles)
    }

    override fun build(project: Project) : VersionResolver {
        return build()
    }

}

data class CompositeVersionResolverBuilder(var resolverBuilders: MutableCollection<Pair<Int, VersionResolverBuilder>>? = mutableListOf()) : VersionResolverBuilder {

    fun add(order: Int, resolver: VersionResolverBuilder) = apply { resolverBuilders!!.add(Pair(order, resolver)) }
    override fun build(): VersionResolver {
        val compositeResolver = CompositeVersionResolverDefault()
        resolverBuilders?.forEach {
            compositeResolver.add(it.first, it.second.build())
        }
        return compositeResolver
    }
    override fun build(project: Project) : VersionResolver {
        val compositeResolver = CompositeVersionResolverDefault()
        resolverBuilders?.forEach {
            compositeResolver.add(it.first, it.second.build(project))
        }
        return compositeResolver
    }
}



