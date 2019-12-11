package com.apgsga.maven.impl.resolver

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.assertThrows
import java.io.File
import org.junit.jupiter.api.Test as test

class ResolverBuilderTest {

    @test
    fun `PatchFileVersionResolverBuilder with File`() {
        val patchFile = File("test")
        val resolverBuilder = PatchFileVersionResolverBuilder().patchFile(patchFile)
        assertEquals(resolverBuilder.patchFile,patchFile)
    }

    @test
    fun `PatchFileVersionResolverBuilder with Parent Dir and Filename before build`() {
        val parentDir = File("build/test")
        val resolverBuilder = PatchFileVersionResolverBuilder().parentDir(parentDir).patchFile("xxxxx")
        assertEquals(parentDir,resolverBuilder.parentDir)
        assertEquals("xxxxx", resolverBuilder.patchFileName)
        assertNull(resolverBuilder.patchFile?.canonicalPath )
        resolverBuilder.build()
    }

    @test
    fun `PatchFileVersionResolverBuilder with Parent Dir and Filename after build`() {
        val parentDir = File("build/test1")
        parentDir.mkdirs()
        File(parentDir,"xxxxx").createNewFile()
        val resolverBuilder = PatchFileVersionResolverBuilder().parentDir(parentDir).patchFile("xxxxx")
        resolverBuilder.build()
        assertEquals(parentDir,resolverBuilder.parentDir)
        assertEquals("xxxxx", resolverBuilder.patchFileName)
        assert(resolverBuilder.patchFile?.canonicalPath!!.endsWith("/build/test1/xxxxx"))

    }

    @test
    fun `PatchFileVersionResolverBuilder with Parent Dir Name and Filename after build`() {
        val parentDir = File("build/test2")
        parentDir.mkdirs()
        File(parentDir,"xxxxx").createNewFile()
        val resolverBuilder = PatchFileVersionResolverBuilder().parentDir("build/test2").patchFile("xxxxx")
        resolverBuilder.build()
        assertEquals(parentDir,resolverBuilder.parentDir)
        assertEquals("xxxxx", resolverBuilder.patchFileName)
        assert(resolverBuilder.patchFile?.canonicalPath!!.endsWith("/build/test2/xxxxx"))
    }

    @test
    fun `PatchFileVersionResolverBuilder with Parent Dir and Parent Dir Name`() {
        val parentDir = File("build/test2")
        parentDir.mkdirs()
        val resolverBuilder = PatchFileVersionResolverBuilder().parentDir("build/test2")
        val exception = assertThrows<IllegalArgumentException > {
            resolverBuilder.parentDir(parentDir)
        }
        assertEquals("Either parentDirName or parentDir" , exception.message)
    }

    @test
    fun `PatchFileVersionResolverBuilder with Patch File and Patch File Name`() {
        val parentDir = File("build/test2")
        parentDir.mkdirs()
        val patchFile =  File(parentDir,"zzzzz")
        val resolverBuilder = PatchFileVersionResolverBuilder().parentDir("build/test2").patchFile(patchFile)
        val exception = assertThrows<IllegalArgumentException > {
            resolverBuilder.patchFile("zzzzz")
        }
        assertEquals("Either patchFile or patchFileName" , exception.message)
    }

}