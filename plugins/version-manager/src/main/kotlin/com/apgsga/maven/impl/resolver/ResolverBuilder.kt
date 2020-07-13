package com.apgsga.maven.impl.resolver

import com.apgsga.maven.MavenBomManager
import com.apgsga.maven.VersionResolver
import com.apgsga.maven.impl.bom.GradleDependencyBomLoader
import com.apgsga.maven.impl.bom.MavenBomManagerDefault
import com.apgsga.maven.impl.bom.RepositoryBomLoader
import com.apgsga.maven.impl.bom.RepositoryFactory
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

data class BomVersionResolverBuilder(
        var bomArtifact: String? = null,
        var recursive: Boolean? = false,
        var repoBaseUrl: String? = null,
        var repoName: String? = null,
        var userName: String? = null,
        var password: String? = null) : VersionResolverBuilder {

    fun bomArtifact(bomArtifact: String) = apply { this.bomArtifact = bomArtifact }
    fun recursive(recursive: Boolean?) = apply { this.recursive = recursive }
    fun repoBaseUrl(repoBaseUrl: String) = apply { this.repoBaseUrl = repoBaseUrl }
    fun repoName(repoName: String) = apply { this.repoName = repoName }
    fun userName(userName: String) = apply { this.userName = userName }
    fun password(password: String) = apply { this.password = password }


    override fun build(): VersionResolver {
        return BomVersionResolver(bomArtifact!!, recursive, mavenBomManagerRepositoryDefault(repoBaseUrl, repoName, userName, password))
    }

    override fun build(project: Project) : VersionResolver {
       return build()
    }

}

data class BomVersionGradleResolverBuilder(
        var bomArtifact: String? = null,
        var recursive: Boolean? = false) : VersionResolverBuilder {

    fun bomArtifact(bomArtifact: String) = apply { this.bomArtifact = bomArtifact }
    fun recursive(recursive: Boolean?) = apply { this.recursive = recursive }

    override fun build(): VersionResolver {
        throw NotImplementedException()
    }

    override fun build(project: Project) : VersionResolver {
       return BomVersionResolver(bomArtifact!!, recursive, MavenBomManagerDefault(GradleDependencyBomLoader(project)))
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

private fun mavenBomManagerRepositoryDefault(repoBaseUrl: String?, repoName: String?, userName: String?, password: String?): MavenBomManager {
    require(!repoBaseUrl.isNullOrEmpty()) { "repoBaseUrl should'nt be null or empty" }
    require(!repoName.isNullOrEmpty()) { "repoName should'nt be null or empty" }
    val repositoryFactory = if (userName == null) {
        RepositoryFactory.createFactory(repoBaseUrl, repoName)
    } else {
        RepositoryFactory.createFactory(repoBaseUrl, repoName, userName, password)
    }
    return MavenBomManagerDefault(RepositoryBomLoader(repositoryFactory.makeRepo()))
}

