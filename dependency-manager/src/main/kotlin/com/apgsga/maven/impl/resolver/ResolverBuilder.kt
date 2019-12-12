package com.apgsga.maven.impl.resolver

import com.apgsga.maven.VersionResolver
import com.apgsga.maven.impl.bom.MavenBomManagerDefault
import com.apgsga.maven.impl.bom.RepositoryFactory
import java.io.File

// TODO (che, 11.12) The Builders will be verified against concrete usage scenarios

interface VersionResolverBuilder {
    fun build(): VersionResolver
}
// TODO (che,11.12) : Can this be done better?
fun <T : VersionResolverBuilder> create(clx : Class<T>): T = clx.newInstance()

data class BomVersionResolverBuilder (
        var bomArtifact: String? = null,
        var recursive: Boolean? = false,
        var repoBaseUrl: String? = null,
        var repoName: String? = null,
        var userName: String? = null,
        var password: String? = null) : VersionResolverBuilder  {

    fun bomArtifact(bomArtifact: String) = apply { this.bomArtifact = bomArtifact }
    fun recursive(recursive: Boolean) = apply { this.recursive = recursive }
    fun repoBaseUrl(repoBaseUrl: String) = apply { this.repoBaseUrl = repoBaseUrl }
    fun repoName(repoName: String) = apply { this.repoName = repoName }
    fun userName(userName: String) = apply { this.userName = userName }
    fun password(password: String) = apply { this.password = password }


    override fun build(): VersionResolver {
        require(!bomArtifact.isNullOrEmpty()) { "bomArtifact should'nt be null or empty" }
        require(!repoBaseUrl.isNullOrEmpty()) { "repoBaseUrl should'nt be null or empty" }
        require(!repoName.isNullOrEmpty()) { "repoName should'nt be null or empty" }
        val repositoryFactory = if (userName == null) {
           RepositoryFactory.createFactory(repoBaseUrl, repoName)}
        else {
            RepositoryFactory.createFactory(repoBaseUrl, repoName, userName, password)
        }
        val bomManager = MavenBomManagerDefault(repositoryFactory.makeRepo())
        return BomVersionResolver(bomArtifact!!, recursive, bomManager)
    }

}

data class PatchFileVersionResolverBuilder(var patchFile: File? = null, var patchFileName: String? = null, var parentDir: File? = null, var parentDirName: String? = null) : VersionResolverBuilder {
    fun patchFile(theFile: File) = apply {
        require(this.patchFileName == null) { "Either patchFile or patchFileName" }
        this.patchFile = theFile
    }
    fun patchFile(patchFile: String) = apply {
        require(this.patchFile == null) { "Either patchFile or patchFileName" }
        this.patchFileName = patchFile
    }
    fun parentDir(parentDir: File) = apply {
        require(this.parentDirName == null) { "Either parentDirName or parentDir" }
        this.parentDir = parentDir
    }
    fun parentDir(parentDir: String) = apply {
        require(this.parentDir == null) { "Either parentDirName or parentDir" }
        this.parentDirName = parentDir
    }
    override fun build(): VersionResolver {
        require(patchFile != null || patchFileName != null) { "patchFile should'nt be null" }
        // TODO (che, 11.12 ) to be verified
        patchFile = if (parentDirName != null) {
            this.parentDir = File(parentDirName)
            File(parentDir, patchFileName)
        } else if (parentDir != null) {
            File(parentDir,patchFileName)
        } else if (patchFileName != null) {
            File(patchFileName)
        } else {
            throw IllegalArgumentException()
        }
        return PatchFileVersionResolver(patchFile!!)
    }

}

data class PatchFileListVersionResolverBuilder(var parentDir: File? = null,var parentDirName: String? = null, var patchFiles: MutableList<File>? = mutableListOf<File>(), var patchComparator: SortedPatchFileListVersionResolver.PatchComparator? = SortedPatchFileListVersionResolver.PatchComparator.PATCHNUMBER_ASC) : VersionResolverBuilder {
    fun parentDir(parentDir: File) = apply {
        require(parentDirName == null) { "Either parentDir or parentDirName" }
        require(this.parentDir == null) { "parentDir can be only set once" }
        this.parentDir = parentDir
    }
    fun parentDir(parentDir: String) = apply {
        require(this.parentDir == null) { "Either parentDir or parentDirName" }
        require(this.parentDirName == null) { "parentDir already set" }
        this.parentDirName = parentDir
        this.parentDir = File(parentDirName)
    }
    fun add(patchFiles: Collection<File>) = apply { this.patchFiles!!.addAll(patchFiles) }
    fun add(patchFile: File) = apply { this.patchFiles!!.add(patchFile) }
    fun add(patchFile: String) = apply {
        require(this.parentDir != null) { "parentDir must be set" }
        this.patchFiles!!.add(File(parentDir, patchFile))
    }
    fun patchComparator(patchComparator: SortedPatchFileListVersionResolver.PatchComparator) = apply { this.patchComparator = patchComparator }

    override fun build(): VersionResolver {
        require(patchFiles != null) { "patchFiles should'nt be null" }
        return SortedPatchFileListVersionResolver(patchFiles!!, patchComparator!!)
    }

}

data class CompositeVersionResolverBuilder(var resolverBuilders: MutableCollection<Pair<Int,VersionResolverBuilder>>? = mutableListOf()) : VersionResolverBuilder {

    fun add(order: Int, resolver: VersionResolverBuilder) = apply { resolverBuilders!!.add(Pair(order,resolver)) }
    override fun build(): VersionResolver {
        val compositeResolver = CompositeVersionResolverDefault()
        resolverBuilders?.forEach{
            compositeResolver.add(it.first,it.second.build())
        }
        return compositeResolver
    }
}

