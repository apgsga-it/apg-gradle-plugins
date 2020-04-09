package com.apgsga.packaging.common.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgServicePackagePlugin
import spock.lang.Shared

import static groovy.io.FileType.FILES

class AppResourcesCopyTaskTests extends AbstractSpecification {
	

	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packaging")
    }

    def "copyRpmScripts works"() {
        given:
        buildFile << """
            plugins {
                id '${ApgServicePackagePlugin.PLUGIN_ID}' 
            }
        """

        when:
        def result = gradleRunnerFactory(['copyAppResources']).build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')	
		new File(testProjectDir,"build/app-pkg/app").exists()
		def dirTarget = new File(testProjectDir,"build/app-pkg/app")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		// TODO (che, 25.9) : haha, test is really terrific
		cntFiles > 4
		// TODO (che, 25.9) : test expected files
	
    }
	
	def "copyRpmScripts with explicit configuration works"() {
		given:
		buildFile << """
            plugins {
                id '${ApgServicePackagePlugin.PLUGIN_ID}' 
            }
		 apgPackage {
			name ="testapp"
            installTarget = "CHTX211"
			mainProgramName  = "com.apgsga.test.SomeMain"
         }
        """

		when:
		def result = gradleRunnerFactory(['copyAppResources']).build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
		new File(testProjectDir,"build/app-pkg/app").exists()
		def dirTarget = new File(testProjectDir,"build/app-pkg/app")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		// TODO (che, 25.9) : haha, test is really terrific
		cntFiles > 4
		// TODO (che, 25.9) : test expected files
	
	}
	
}


