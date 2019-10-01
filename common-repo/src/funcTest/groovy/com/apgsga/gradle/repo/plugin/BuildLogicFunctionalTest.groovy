package com.apgsga.gradle.repo.plugin

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class BuildLogicFunctionalTest extends Specification {
	
    File testProjectDir
    File buildFile

	
	def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile();
		println "Project Dir : ${testProjectDir.absolutePath}"
		buildFile = new File(testProjectDir,'build.gradle')
	}
	
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
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments( 'init','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
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
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments( 'init','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
    }
	
	
	def "Common Repo Plugin works explicit ovrriding defaults"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.common.repo' 
            }
			apgArtifactoryRepo {
				repoBaseUrl = "xxxx"
				repoName = "remoteRepoName"
				user = "abc"
				password = "def"
			}
			apgLocalRepo {
				repoBaseUrl = "otherdirectorty"
				repoName = "testrepo"
			}
			apgLocalRepo.log()
			apgArtifactoryRepo.log()
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments( 'init','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
}

