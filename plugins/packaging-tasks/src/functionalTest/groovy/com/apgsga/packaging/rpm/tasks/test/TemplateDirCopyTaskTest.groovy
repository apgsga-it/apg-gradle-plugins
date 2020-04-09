package com.apgsga.packaging.rpm.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgRpmPackagePlugin

class TemplateDirCopyTaskTest extends AbstractSpecification {

    def "copy template Dir works"() {
        given:
        buildFile << """
            plugins {
                id '${ApgRpmPackagePlugin.PLUGIN_ID}' 
            }
        """

        when:
        def result = gradleRunnerFactory(['templateDirCopy']).build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
		def appDirSource = new File(testProjectDir,"build/packaging/app")
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
		def rpmDirSource = new File(testProjectDir,"build/packaging/rpm")
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


