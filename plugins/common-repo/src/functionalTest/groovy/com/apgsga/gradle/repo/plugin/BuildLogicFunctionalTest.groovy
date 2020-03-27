package com.apgsga.gradle.repo.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification


class BuildLogicFunctionalTest extends AbstractSpecification {

	def "Explicit test that local repo has a local URL"() {
		given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgRepos.get(LOCAL).log()				
			"""
		when:
			def result = gradleRunnerFactory(['init']).build()
		then:
			println "Result output: ${result.output}"
			!result.output.contains('repoBaseUrl=https')
			!result.output.contains('repoBaseUrl=http')
		}
	
	def "Common Repo Plugin works with defaults"() {
		given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgRepos.get(LOCAL).log()
				apgRepos.get(ZIP).log()
				apgRepos.get(RPM).log()
				apgRepos.get(MAVEN).log()
				apgRepos.get(MAVEN_SNAPSHOT).log()
				apgRepos.get(MAVEN_RELEASE).log()
				apgRepos.get(JAVA_DIST).log()
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
			result.output.contains('repoName=apgPlatformDependencies-test')
	}

    def "Common Repo Plugin works explicit with Defaults"() {
        given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgRepos{
				}
				apgRepos.get(LOCAL).log()
				apgRepos.get(ZIP).log()
				apgRepos.get(RPM).log()
				apgRepos.get(MAVEN).log()
				apgRepos.get(MAVEN_SNAPSHOT).log()
				apgRepos.get(MAVEN_RELEASE).log()
				apgRepos.get(JAVA_DIST).log()
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
			result.output.contains('repoName=apgPlatformDependencies-test')
    }
	
	
	def "Common Repo Plugin works explicit overriding defaults"() {
		given:
			buildFile << """
				plugins {
					id 'com.apgsga.common.repo' 
				}
				apgRepos{
					config(LOCAL,[REPO_NAME:"thisIsMyLocalRepo"])
					config(MAVEN_RELEASE,[REPO_NAME:"release_2",REPO_USER:"bob"])	
				}
				apgRepos.get(LOCAL).log()
				apgRepos.get(ZIP).log()
				apgRepos.get(RPM).log()
				apgRepos.get(MAVEN).log()
				apgRepos.get(MAVEN_SNAPSHOT).log()
				apgRepos.get(MAVEN_RELEASE).log()
				apgRepos.get(JAVA_DIST).log()
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
			result.output.contains('repoName=apgPlatformDependencies-test')
	}

	def "Default repoBaseUrl, default user and default password work"() {
		// JHE: we arbitrarly test against maven Repo
		given:
			buildFile << """
					plugins {
						id 'com.apgsga.common.repo' 
					}
					apgRepos.get(MAVEN).log()
				"""
		when:
			def result = gradleRunnerFactory(['init']).build()
		then:
			println "Result output: ${result.output}"
			result.output.contains('repoName=maven')
			result.output.contains('user=gradledev-tests-user')
			result.output.contains('password=xxxxxx')
			result.output.contains('repoBaseUrl=https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga')
	}
}