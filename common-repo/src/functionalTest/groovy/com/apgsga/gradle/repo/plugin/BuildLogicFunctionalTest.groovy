package com.apgsga.gradle.repo.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class BuildLogicFunctionalTest extends AbstractSpecification {
	
	def "Common Repo Plugin works with defaults"() {
		given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgLocalRepo.log()
				apgArtifactoryRepo.log()
			"""

		when:
			def result = gradleRunnerFactory(['init']).build()
		then:
			println "Result output: ${result.output}"
			result.output.contains('release-functionaltest')
			result.output.contains('rpm-functionaltest')
			result.output.contains('snapshot-functionaltest')
	}

    def "Common Repo Plugin works explicit with Defaults"() {
        given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgArtifactoryRepo {
				}
				apgLocalRepo {
				}
				apgLocalRepo.log()
				apgArtifactoryRepo.log()
			"""
        when:
        	def result = gradleRunnerFactory(['init']).build()
        then:
			println "Result output: ${result.output}"
			result.output.contains('release-functionaltest')
			result.output.contains('rpm-functionaltest')
			result.output.contains('snapshot-functionaltest')
    }
	
	
	def "Common Repo Plugin works explicit overriding defaults"() {
		given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgArtifactoryRepo {
					defaultRepoNames[MAVEN_RELEASE] = "YYYYY"
					defaultRepoNames[MAVEN_SNAPSHOT] = "BBBBB"
					repoBaseUrl = "xxxx"
					user = "abc"
					password = "def"
				}
				apgLocalRepo {
					repoBaseUrl = "otherdirectory"
					defaultRepoNames[MAVEN_RELEASE] = "testrepo"
				}
				apgLocalRepo.log()
				apgArtifactoryRepo.log()
	        """
		when:
			def result = gradleRunnerFactory(['init']).build()
		then:
			println "Result output: ${result.output}"
			result.output.contains('MAVEN_SNAPSHOT=BBBBB')
			result.output.contains('MAVEN_RELEASE=YYYYY')
			result.output.contains('MAVEN_RELEASE=testrepo')
			result.output.contains('MAVEN_SNAPSHOT=snapshot-functionaltest')
			!result.output.contains('MAVEN_RELEASE=release-functionaltest')
	}
}