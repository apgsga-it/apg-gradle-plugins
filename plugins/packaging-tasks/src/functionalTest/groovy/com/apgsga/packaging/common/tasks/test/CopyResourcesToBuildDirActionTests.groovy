package com.apgsga.packaging.common.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgServicePackagePlugin

class CopyResourcesToBuildDirActionTests extends AbstractSpecification {

    def "copy resources to build Dir works"() {
        given:
        buildFile << """
            plugins {
                id '${ApgServicePackagePlugin.PLUGIN_ID}' 
            }
        """

        when:
        def result = gradleRunnerFactory(['copyCommonPackagingResources']).build()
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


