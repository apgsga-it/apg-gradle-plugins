package com.apgsga.gradle.repo.config.plugin

import java.nio.file.Files

import org.gradle.BuildResult
import org.gradle.testkit.runner.GradleRunner

import spock.lang.Specification

class BuildLogicFunctionalTest extends Specification {
	
	File testProjectDir
	File buildFile

	def setup() {
		println "Running setup"
		String tempDir = System.getProperty("java.io.tmpdir");
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
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile();
		println "Project Dir : ${testProjectDir.absolutePath}"
		buildFile = new File(testProjectDir,'build.gradle')
	}
	
	def "Repo Config works with defaults"() {
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				task listrepos {
				    doLast {
				        println "Repositories:"
				        project.repositories.each { repo -> 
							println "Repo Name: " + repo.name
						    println "Repo Url: " + repo.url 
					    }
						println "Done"
				   }
				}
	        """
		when:
			def result = GradleRunner.create()
				.withProjectDir(testProjectDir)
				.withArguments( 'listrepos')
				.withPluginClasspath()
				.build()
				
		then:
			println "Result output: ${result.output}"
			result.output.contains('MavenLocal')
			result.output.contains('rpm-functionaltest')
			result.output.contains('MavenRepo')
	}
}
