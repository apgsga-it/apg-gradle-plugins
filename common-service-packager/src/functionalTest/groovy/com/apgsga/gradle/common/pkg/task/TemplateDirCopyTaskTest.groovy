package com.apgsga.gradle.common.pkg.task

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class TemplateDirCopyTaskTest extends Specification {
	
    File testProjectDir
    File buildFile

	def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile()
		println "Project Dir : ${testProjectDir.absolutePath}"
		buildFile = new File(testProjectDir,'build.gradle')
	}

    def "copy template Dir works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.common.package' 
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('templateDirCopy','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
		def appDirSource = new File(testProjectDir,"build/packageing/app")
		def countSrcAppDir = 0
		appDirSource.traverse {
			countSrcAppDir++
		}
		def appDirTarget = new File(testProjectDir,"build/template/app")
		def countTargetAppDir = 0
		appDirTarget.traverse {
			countTargetAppDir++
		}
		countSrcAppDir == countTargetAppDir
		!(new File(testProjectDir,"build/template/appconfigs").exists())
		!(new File(testProjectDir,"build/template/resources").exists())
		
		
    }
	

	
}


