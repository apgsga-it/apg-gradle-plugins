package com.apgsga.gradle.repo.config.plugin

import java.nio.file.Files
import java.nio.file.Paths

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
	
	def "Repo Config works with all repositiories configured"() {
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apgRepoConfig {
					local()
					mavenLocal()
					artifactory()
					mavenCentral()
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
			result.output.contains('MavenLocal2')
			result.output.contains('rpm-functionaltest')
			result.output.contains('MavenRepo')
	}
	
	def "Repo Config works with only 2 repositories configured"() {
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apgRepoConfig {
					local()
					mavenCentral()
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
			!result.output.contains('MavenLocal2')
			!result.output.contains('rpm-functionaltest')
			result.output.contains('MavenRepo')
	}
	
	def "Repo Config works with one concrete dependency"() {
		given:
			buildFile << """
				apply plugin: 'java'
				
				dependencies {
					implementation 'junit:junit:4.12'
				}
	        """
		when:
			def result = GradleRunner.create()
				.withProjectDir(testProjectDir)
				.withArguments('clean','build','--info','--stacktrace')
				.withPluginClasspath()
				.build()
				
		then:
			println "Result output: ${result.output}"
	}
}
