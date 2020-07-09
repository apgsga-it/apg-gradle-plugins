package com.apgsga.maven.impl.resolver

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.assertThrows
import java.io.File
import org.junit.jupiter.api.Test as test

class ResolverBuilderTest {

    val dirSeperator = File.separator!!

    /*
    @test
    fun `PatchFileVersionResolverBuilder with File`() {
        val patchFile = File("test")
        val resolverBuilder = PatchFileVersionResolverBuilder().patchFile(patchFile)
        assertEquals(resolverBuilder.patchFile,patchFile)
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
        println("dirSeperator:$dirSeperator")
        assert(resolverBuilder.patchFile?.canonicalPath!!.endsWith("${dirSeperator}build${dirSeperator}test1${dirSeperator}xxxxx"))

    }

    @test
    fun `PatchFileVersionResolverBuilder Filename only after build`() {
        val parentDir = File("build/test2")
        parentDir.mkdirs()
        File(parentDir,"xxxxx").createNewFile()
        val resolverBuilder = PatchFileVersionResolverBuilder().patchFile("build/test2/xxxxx")
        resolverBuilder.build()
        assertNull(resolverBuilder.parentDir)
        assertNull(resolverBuilder.parentDirName)
        assertEquals("build/test2/xxxxx", resolverBuilder.patchFileName)
        assert(resolverBuilder.patchFile?.canonicalPath!!.endsWith("${dirSeperator}build${dirSeperator}test2${dirSeperator}xxxxx"))

    }

    @test
    fun `PatchFileVersionResolverBuilder with Parent Dir Name and Filename after build`() {
        val parentDir = File("build/test3")
        parentDir.mkdirs()
        File(parentDir,"xxxxx").createNewFile()
        val resolverBuilder = PatchFileVersionResolverBuilder().parentDir("build/test3").patchFile("xxxxx")
        resolverBuilder.build()
        assertEquals(parentDir,resolverBuilder.parentDir)
        assertEquals("xxxxx", resolverBuilder.patchFileName)
        assert(resolverBuilder.patchFile?.canonicalPath!!.endsWith("${dirSeperator}build${dirSeperator}test3${dirSeperator}xxxxx"))
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
        val parentDir = File("build/test4")
        parentDir.mkdirs()
        val patchFile =  File(parentDir,"zzzzz")
        val resolverBuilder = PatchFileVersionResolverBuilder().parentDir("build/test4").patchFile(patchFile)
        val exception = assertThrows<IllegalArgumentException > {
            resolverBuilder.patchFile("zzzzz")
        }
        assertEquals("Either patchFile or patchFileName" , exception.message)
    }
    */


}