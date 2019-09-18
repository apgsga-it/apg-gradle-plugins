package com.apgsga.gradle.publish

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
	@Shared File rpmToPublish
	@Shared File tarToPublish

    def setupSpec() {
		rpmToPublish = new File("src/funcTest/resources/apg-plugintests-0.8.9-1.noarch.rpm")
		tarToPublish = new File("src/funcTest/resources/app-plugintests-0.1.tar.gz")
    } 
	
	def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile();
		println "Project Dir : ${testProjectDir.absolutePath}"
		buildFile = new File(testProjectDir,'build.gradle')
	}

    def "publish rpm to local works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgPublishConfig {
				artefactFile = "${rpmToPublish.absolutePath}" 
				remoteRepo {
					publish = false
				}
				localRepo {
					publish = true
				}
			}
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('apgPublish','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
    }
	
	
	def "publish rpm to remote works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgPublishConfig {
				artefactFile = "${rpmToPublish.absolutePath}" 
			}
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('apgPublish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
	def "publish rpm to both local and remote works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgPublishConfig {
				artefactFile = "${rpmToPublish.absolutePath}"
				localRepo {
					publish = true
				} 
			}
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('apgPublish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
	def "publish tarball to both local and remote works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgPublishConfig {
				artefactFile = "${tarToPublish.absolutePath}"
				localRepo {
					publish = true
				} 
			}
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('apgPublish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
}

