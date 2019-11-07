package com.apgsga.gradle.test.utils

import spock.lang.Specification

import java.nio.file.Files

abstract class AbstractSpecification extends Specification {

    protected File testProjectDir
    protected File buildFile

    def setup() {
        println "Running setup"
        String tempDir = System.getProperty("java.io.tmpdir")
        println tempDir
        def tempDirFile = new File(tempDir)
        def testDirsToDelete = []
        tempDirFile.eachDir { file ->
            if (file.name.startsWith("gradletestproject")) {
                testDirsToDelete.add(file)
            }
        }
        testDirsToDelete.each { testDir ->
            println "About to delete ${testDir}"
            testDir.deleteDir()
        }
        testProjectDir = Files.createTempDirectory('gradletestproject').toFile()
        File buildDir = new File(testProjectDir,'build')
        buildDir.mkdirs()
        println "Project Dir : ${testProjectDir.absolutePath}"
        buildFile = new File(testProjectDir,"build.gradle" + buildTyp())
    }

    // may be overwritten with kts
    protected def buildTyp() {
        ""
    }

}