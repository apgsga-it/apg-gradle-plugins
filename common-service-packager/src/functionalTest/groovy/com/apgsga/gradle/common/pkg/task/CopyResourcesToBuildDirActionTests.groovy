package com.apgsga.gradle.common.pkg.task

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner

class CopyResourcesToBuildDirActionTests extends AbstractSpecification {

    def "copy resources to build Dir works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.common.package' 
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('copyCommonPackagingResources','--info', '--stacktrace')
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


