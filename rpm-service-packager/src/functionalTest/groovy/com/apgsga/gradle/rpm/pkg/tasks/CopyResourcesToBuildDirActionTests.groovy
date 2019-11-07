package com.apgsga.gradle.rpm.pkg.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner

import java.nio.file.Files

class CopyResourcesToBuildDirActionTests extends AbstractSpecification {
	
    File testProjectDir
    File buildFile

	def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile()
		println "Project Dir : ${testProjectDir.absolutePath}"
		buildFile = new File(testProjectDir,'build.gradle')
	}

    def "copy resources to build Dir works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('copyRpmPackagingResources','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
		def packagingSrc = new File("src/main/resources/packageing")
		def cntPackagingSrc  = 0
		packagingSrc.traverse {
			cntPackagingSrc++
		}
		def packagingTarget = new File(testProjectDir,"build/packageing")
		def cntTarget = 0
		packagingTarget.traverse {
			cntTarget++
		}
		cntTarget == cntPackagingSrc
		
    }
	

	
}


