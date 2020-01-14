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
        // TODO JHE: Might not be needed anymore with IT-35189
        createGradleProperties()
        // TODO JHE: Might not be needed anymore with IT-35189
        createGradleEncryptedProperties()
        setupRepoNameJson()
        setupSupportedServicesJson()
    }

    private def createBuildFile() {
        buildFile = new File(testProjectDir,"build.gradle" + buildTyp())
        buildFile << getDefaultBuildFileContent()
    }

    private static def getDefaultBuildFileContent() {
        return """
        import static com.apgsga.gradle.repo.extensions.RepoType.*
        import static com.apgsga.gradle.repo.extensions.RepoProperties.*

        """
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
        println "${gradleProperties.absolutePath} has been created!"
    }

    def createGradleEncryptedProperties() {
        File gradleEncyrpytedProperties = new File("${gradleHomeDirPath}/gradle.encrypted.properties")
        gradleEncyrpytedProperties << "apg_install=tAAMlNlXrCjnphgKtCkHHZLkezTSeM1QwZ4+kfXyMaM\\="
        println "${gradleEncyrpytedProperties.absolutePath} has been created!"
    }

    def setupSupportedServicesJson() {
        File installableServicesJson = new File(gradleHomeDirPath, "supportedServices.json")
        initSupportedServicesJsonContent(installableServicesJson)
    }

    def initSupportedServicesJsonContent(File file) {
        file << """
            {
                "supportedServices": ["jadas","digiflex","vkjadas","interjadas","interweb","testapp"]
            }
        """
    }

    private def setupRepoNameJson() {
        File repoNamesJson = new File(gradleHomeDirPath, "repoNames.json")
        initTepoNamesJsonContent(repoNamesJson)
    }

    private static def initTepoNamesJsonContent(File repoNames) {
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
    },
    {
      "LOCAL": "local"
    },
    {
      "MAVEN": "maven"
    },
    {
      "JAVA_DIST": "apgPlatformDependencies-test"
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