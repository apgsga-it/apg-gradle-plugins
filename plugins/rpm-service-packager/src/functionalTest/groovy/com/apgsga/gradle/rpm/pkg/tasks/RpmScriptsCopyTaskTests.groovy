package com.apgsga.gradle.rpm.pkg.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import spock.lang.Shared

import static groovy.io.FileType.FILES

class RpmScriptsCopyTaskTests extends AbstractSpecification {

	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packageing")
    }

    def "copyRpmScripts works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
            }
        """

        when:
        def result = gradleRunnerFactory(['copyRpmScripts']).build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')	
		new File(testProjectDir,"build/rpm").exists()
		def dirTarget = new File(testProjectDir,"build/rpm")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		cntFiles == 4
		// TODO (che, 25.9) : test expected files and content
	
    }
	
	
	
}

