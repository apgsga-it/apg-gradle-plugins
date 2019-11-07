package com.apgsga.gradle.rpm.pkg.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class TemplateDirCopyTaskTest extends AbstractSpecification {

    def "copy template Dir works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
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
		def rpmDirSource = new File(testProjectDir,"build/packageing/rpm")
		def countSrcRpmDir = 0
		rpmDirSource.traverse {
			countSrcRpmDir++
		}
		def rpmDirTarget = new File(testProjectDir,"build/template/rpm")
		def countTargetRpmDir = 0
		rpmDirTarget.traverse {
			countTargetRpmDir++
		}
		countSrcRpmDir == countTargetRpmDir
		!(new File(testProjectDir,"build/template/appconfigs").exists())
		!(new File(testProjectDir,"build/template/resources").exists())
		
		
    }
	

	
}


