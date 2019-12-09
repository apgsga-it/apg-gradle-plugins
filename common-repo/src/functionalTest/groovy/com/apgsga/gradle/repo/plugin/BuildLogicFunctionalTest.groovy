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
				apgReposConfig.get(LOCAL).log()
				apgReposConfig.get(ZIP).log()
				apgReposConfig.get(RPM).log()
				apgReposConfig.get(MAVEN).log()
				apgReposConfig.get(MAVEN_SNAPSHOT).log()
				apgReposConfig.get(MAVEN_RELEASE).log()
				apgReposConfig.get(JAVA_DIST).log()
			"""

		when:
			def result = gradleRunnerFactory(['init']).build()
		then:
			println "Result output: ${result.output}"
			result.output.contains('repoName=release-functionaltest')
			result.output.contains('repoName=rpm-functionaltest')
			result.output.contains('repoName=snapshot-functionaltest')
			result.output.contains('repoName=local')
			result.output.contains('repoName=maven')
			result.output.contains('repoName=java-dist')
	}

    def "Common Repo Plugin works explicit with Defaults"() {
        given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgReposConfig{
				}
				apgReposConfig.get(LOCAL).log()
				apgReposConfig.get(ZIP).log()
				apgReposConfig.get(RPM).log()
				apgReposConfig.get(MAVEN).log()
				apgReposConfig.get(MAVEN_SNAPSHOT).log()
				apgReposConfig.get(MAVEN_RELEASE).log()
				apgReposConfig.get(JAVA_DIST).log()
			"""
        when:
        	def result = gradleRunnerFactory(['init']).build()
        then:
			println "Result output: ${result.output}"
			result.output.contains('repoName=release-functionaltest')
			result.output.contains('repoName=rpm-functionaltest')
			result.output.contains('repoName=snapshot-functionaltest')
			result.output.contains('repoName=local')
			result.output.contains('repoName=maven')
			result.output.contains('repoName=java-dist')
    }
	
	
	def "Common Repo Plugin works explicit overriding defaults"() {
		given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgReposConfig{
					set(LOCAL,[REPO_NAME:"thisIsMyLocalRepo"])
					set(MAVEN_RELEASE,[REPO_NAME:"release_2",REPO_USER:"bob"])	
				}
				apgReposConfig.get(LOCAL).log()
				apgReposConfig.get(ZIP).log()
				apgReposConfig.get(RPM).log()
				apgReposConfig.get(MAVEN).log()
				apgReposConfig.get(MAVEN_SNAPSHOT).log()
				apgReposConfig.get(MAVEN_RELEASE).log()
				apgReposConfig.get(JAVA_DIST).log()
	        """
		when:
			def result = gradleRunnerFactory(['init']).build()
		then:
			println "Result output: ${result.output}"
			result.output.contains('repoName=release-functionaltest')
			result.output.contains('repoName=release_2')
			result.output.contains('user=bob')
			result.output.contains('repoName=rpm-functionaltest')
			result.output.contains('repoName=snapshot-functionaltest')
			!result.output.contains('repoName=local')
			result.output.contains('repoName=thisIsMyLocalRepo')
			result.output.contains('repoName=maven')
			result.output.contains('repoName=java-dist')
	}
}