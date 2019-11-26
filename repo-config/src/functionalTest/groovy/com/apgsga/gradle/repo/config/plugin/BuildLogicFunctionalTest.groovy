package com.apgsga.gradle.repo.config.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification

import java.nio.file.Files
import java.nio.file.Paths

import org.gradle.BuildResult
import org.gradle.testkit.runner.GradleRunner

import spock.lang.Specification

class BuildLogicFunctionalTest extends AbstractSpecification {
	
	def "Repo Config works with all default repositiories configured"() {
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apgRepository {
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
			def result = gradleRunnerFactory(['listrepos']).build()
		then:
			println "Result output: ${result.output}"
			result.output.contains('MavenLocal')
			result.output.contains('MavenLocal2')
			result.output.contains('release-functionaltest')
			result.output.contains('MavenRepo')
	}
	
	def "Repo Config works with only 2 repositories configured"() {
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apgRepository {
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
			def result = gradleRunnerFactory(['listrepos']).build()
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

	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apply plugin: 'java'

				apgRepository {
					local()
					mavenCentral()
				}
				
				dependencies {
					implementation 'junit:junit:4.12'
				}
	        """
		when:
			def result = gradleRunnerFactory(['clean','build']).build()
		then:
			println "Result output: ${result.output}"
	}
	
	def "Repo Config works with repositories name coming from map"() {
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apgArtifactoryRepo {
					defaultRepoNames[RPM] = "thisIsMyGenericTestRepo"
					defaultRepoNames[MAVEN_RELEASE] = "thisIsMyReleaseTestRepo"
				}

				apgRepository {
					artifactory()
				}
	        """
		when:
			def result = gradleRunnerFactory(['init']).build()
		then:
			println "Result output: ${result.output}"
			!result.output.contains('rpm-functionaltest')
			result.output.contains('thisIsMyGenericTestRepo')
			result.output.contains('thisIsMyReleaseTestRepo')
	}
}
