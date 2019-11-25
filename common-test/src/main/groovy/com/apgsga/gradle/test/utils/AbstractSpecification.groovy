package com.apgsga.gradle.test.utils

import org.gradle.testkit.runner.GradleRunner
import org.springframework.core.io.ClassPathResource
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

abstract class AbstractSpecification extends Specification {

    protected File testProjectDir
    protected File buildFile
    protected String gradleHomeDirPath

    def setup() {
        println "Running setup"
        deletePreviousTestFolders()
        setupTestProjectDir()
        createBuildFile()
        setupRepoNameJson()
    }

    private def createBuildFile() {
        buildFile = new File(testProjectDir,"build.gradle" + buildTyp())
        buildFile << getDefaultBuildFileContent()
    }

    private def getDefaultBuildFileContent() {
        return """
        import com.apgsga.gradle.repo.extensions.RepoNames

        buildscript {
            repositories {
                mavenLocal()
            }
            dependencies {
                classpath group: 'com.apgsga.gradle', name: 'common-repo', version: '+'
            }
        }
        """
    }

    private def deletePreviousTestFolders() {
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
    }

    private def setupTestProjectDir() {
        testProjectDir = Files.createTempDirectory('gradletestproject').toFile()
        File buildDir = new File(testProjectDir,'build')
        buildDir.mkdirs()
        println "Project Dir : ${testProjectDir.absolutePath}"
    }

    private def setupRepoNameJson() {
        gradleHomeDirPath = "${testProjectDir}${File.separator}.gradle${File.separator}"
        File gradleHomeDir = new File(gradleHomeDirPath)
        gradleHomeDir.mkdirs()
        println "Gradle Home directory for tests: ${gradleHomeDir.absolutePath}"
        File repoNamesJson = new File(gradleHomeDir, "repoNames.json")
        initTepoNamesJsonContent(repoNamesJson)
    }

    private def initTepoNamesJsonContent(File repoNames) {
        repoNames << """

    {
  "repos": [
    {
      "ZIP": "release-functionaltest"
    },
    {
      "RPM": "rpm-functionaltest"
    },
    {
      "MAVEN_SNAPSHOT": "snapshot-functionaltest"
    },
    {
      "MAVEN_RELEASE": "release-functionaltest"
    }
  ],
  "repoUserName": "gradledev-tests-user",
  "repoUserPwd": "gradledev-tests-user",
  "repoBaseUrl": "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga"
}
"""
    }

    protected def gradleRunnerFactory() {
        return gradleRunnerFactory([])
    }

    protected def gradleRunnerFactory(List<String> specificTestArguments) {
        specificTestArguments.addAll(getDefaultArguments())
        return GradleRunner.create()
                .withArguments(specificTestArguments)
                .withPluginClasspath()
                .withProjectDir(testProjectDir)
    }

    private List<String> getDefaultArguments() {
        // Cast to String to avoid java-lang.ArayStoreException
        return [(String) "-Dgradle.user.home=${gradleHomeDirPath}", "--info", "--stacktrace"]
    }

// may be overwritten with kts
    protected def buildTyp() {
        ""
    }

}