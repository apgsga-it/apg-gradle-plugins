package com.apgsga.gradle.test.utils

import org.springframework.core.io.ClassPathResource
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

abstract class AbstractSpecification extends Specification {

    protected File testProjectDir
    protected File buildFile
    protected String gradleHomeDir

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
        setupRepoNameJson()
    }

    def setupRepoNameJson() {
        gradleHomeDir = "${System.getProperty('user.home')}${File.separator}.gradle${File.separator}"
        if(!(new File(gradleHomeDir).exists())) {
            Files.createDirectory(gradleHomeDir)
        }
        ClassPathResource cpr = new ClassPathResource("repoNames.json")
        Files.copy(cpr.getInputStream(), Paths.get(gradleHomeDir + File.separator + "repoNames.json"), StandardCopyOption.REPLACE_EXISTING)
    }
// may be overwritten with kts
    protected def buildTyp() {
        ""
    }

}