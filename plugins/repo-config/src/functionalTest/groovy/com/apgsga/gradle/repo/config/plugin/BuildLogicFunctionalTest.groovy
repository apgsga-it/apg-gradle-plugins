package com.apgsga.gradle.repo.config.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification

class BuildLogicFunctionalTest extends AbstractSpecification {
	
	def "Repo Config works with all default repositiories configured"() {
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apgRepositories {
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

				apgRepositories {
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
			result.output.contains('release-functionaltest')
			result.output.contains('MavenRepo')
	}
	
	def "Repo Config works with one concrete dependency"() {
		given:
			buildFile << """

	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apply plugin: 'java'

				apgRepositories {
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
	
	def "Repo Config works with repositories name configured with non default"() {
		given:
			buildFile << """
	            plugins {
	                id 'com.apgsga.gradle.repo.config'
	            }

				apgRepos {
					config(RPM,[REPO_NAME:"thisIsMyGenericTestRepo"])
					config(MAVEN_RELEASE,[REPO_NAME:"thisIsMyReleaseTestRepo"])
				}
				apgRepos.get(RPM).log()
				apgRepos.get(MAVEN_RELEASE).log()

				apgRepositories {
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
