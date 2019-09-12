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

    def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile();
		println "Project Dir : ${testProjectDir.absolutePath}"
        buildFile = new File(testProjectDir,'build.gradle')
    } 

    def "publish rpm works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpmpublish' 
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('apgRpmPublish','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
    }
	
	
}


