package com.apgsga.gradle.rpmpublish

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class BuildLogicFunctionalTest extends Specification {
	
    File testProjectDir
    File buildFile
	File rpmToPublish

    def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile();
		rpmToPublish = new File("src/funcTest/resources/apg-plugintests-0.8.9-1.noarch.rpm")
		println "Project Dir : ${testProjectDir.absolutePath}"
        buildFile = new File(testProjectDir,'build.gradle')
    } 

    def "publish rpm works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpmpublish' 
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
	
	
}


