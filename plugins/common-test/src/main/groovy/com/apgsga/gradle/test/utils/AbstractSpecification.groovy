package com.apgsga.gradle.test.utils

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import java.nio.file.Files

abstract class AbstractSpecification extends Specification {

    protected File testProjectDir
    protected File buildFile
    protected String gradleHomeDirPath

    def setup() {
        println "Running setup"
        deletePreviousTestFolders()
        setupTestProjectDir()
        createBuildFile()
        createGradleHomeDir()
        createGradleProperties()
        createGradleEncryptedProperties()
    }

    private def createBuildFile() {
        buildFile = new File(testProjectDir,"build.gradle" + buildTyp())
    }

    private static def deletePreviousTestFolders() {
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

    def createGradleHomeDir() {
        gradleHomeDirPath = "${testProjectDir}${File.separator}.gradle${File.separator}"
        File gradleHomeDir = new File(gradleHomeDirPath)
        gradleHomeDir.mkdirs()
        println "Gradle Home directory for tests: ${gradleHomeDir.absolutePath}"
    }

    def createGradleProperties() {
        File gradleProperties = new File("${gradleHomeDirPath}/gradle.properties")
        gradleProperties << "revision.file.path=src/functionalTest/resources/Revisions.json"
        gradleProperties << System.getProperty("line.separator")
        gradleProperties << "config.dir=src/functionalTest/resources/config"
        gradleProperties << System.getProperty("line.separator")
        gradleProperties << "target.system.mapping.file.name=TargetSystemMappings.json"
        gradleProperties << System.getProperty("line.separator")
        gradleProperties << "mavenRepoBaseUrl=https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga"
        gradleProperties << System.getProperty("line.separator")
        gradleProperties << "apg.common.repo.maven.file.path=src/functionalTest/resources/testMavenSettings.xml"
        gradleProperties << System.getProperty("line.separator")
        gradleProperties << "apg.common.repo.gradle.maven.active.profile=artifactory-test"
        gradleProperties << System.getProperty("line.separator")
        gradleProperties << "apg.common.repo.gradle.export.gradle.props=true"
        println "${gradleProperties.absolutePath} has been created!"
    }

    def createGradleEncryptedProperties() {
        File gradleEncyrpytedProperties = new File("${gradleHomeDirPath}/gradle.encrypted.properties")
        gradleEncyrpytedProperties << "apg_install=tAAMlNlXrCjnphgKtCkHHZLkezTSeM1QwZ4+kfXyMaM\\="
        gradleEncyrpytedProperties << System.getProperty("line.separator")
        gradleEncyrpytedProperties << "mavenRepoUser=jTgfLqung5Pw4VQqSldeUEK9NG/HslI+GjGZ2aqtdx8\\="
        gradleEncyrpytedProperties << System.getProperty("line.separator")
        gradleEncyrpytedProperties << "mavenRepoPwd=jTgfLqung5Pw4VQqSldeUEK9NG/HslI+GjGZ2aqtdx8\\="
        println "${gradleEncyrpytedProperties.absolutePath} has been created!"
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
                .withDebug(true)
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