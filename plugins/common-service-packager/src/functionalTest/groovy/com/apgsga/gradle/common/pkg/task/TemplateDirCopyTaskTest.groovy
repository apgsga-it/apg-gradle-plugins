package com.apgsga.gradle.common.pkg.task

import com.apgsga.gradle.test.utils.AbstractSpecification

class TemplateDirCopyTaskTest extends AbstractSpecification {

    def "copy template Dir works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.service.package' 
            }
        """

        when:
        def result = gradleRunnerFactory(['templateDirCopy']).build()
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


