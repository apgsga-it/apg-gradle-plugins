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
			def result = GradleRunner.create()
				.withProjectDir(testProjectDir)
				.withArguments('clean','build','--info','--stacktrace')
				.withPluginClasspath()
				.build()
				
		then:
			println "Result output: ${result.output}"
	}
	
	def "Repo Config works with repositories name coming from map"() {
		
		// TODO JHE: Well, not sure I got it correctly, how can we configure the artifactory Repo differently within the same build?
		
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
					id 'com.apgsga.maven.publish'
	            }

				apgArtifactoryRepo {
					repoName = getAvailableRepoNames().get('rpm-publish')
				}

				apgRepository {
					artifactory()
				}

				apgMavenPublish {
					artifactory()
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
			!result.output.contains('MavenLocal')
			!result.output.contains('MavenLocal2')
			!result.output.contains('rpm-functionaltest')
			!result.output.contains('MavenRepo')
			result.output.contains('yumpatchrepo')
	}
}
