package com.apgsga.packaging.rpm.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgRpmPackagePlugin

class CopyResourcesToBuildDirActionTests extends AbstractSpecification {

    def "copy resources to build Dir works"() {
        given:
        buildFile << """
            plugins {
                id '${ApgRpmPackagePlugin.PLUGIN_ID}' 
            }
        """

        when:
        def result = gradleRunnerFactory(['copyRpmPackagingResources']).build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
		def packagingSrc = new File("src/main/resources/packaging")
		def cntPackagingSrc  = 0
		packagingSrc.traverse {
			cntPackagingSrc++
		}
		def packagingTarget = new File(testProjectDir,"build/packaging")
		def cntTarget = 0
		packagingTarget.traverse {
			cntTarget++
		}
		cntTarget == cntPackagingSrc
		
    }
	

	
}


