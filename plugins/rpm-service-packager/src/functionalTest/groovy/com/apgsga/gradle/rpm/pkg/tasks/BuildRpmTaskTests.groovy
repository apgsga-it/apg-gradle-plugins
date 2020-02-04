package com.apgsga.gradle.rpm.pkg.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import spock.lang.Shared

import static groovy.io.FileType.FILES

class BuildRpmTaskTests extends AbstractSpecification {

	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packageing")
    } 


    def "buildRpm works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpm.package'
            }
			apgRepositories {
				mavenLocal()
				mavenCentral()
			}

		// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
        apgPackage {
			serviceName ="testapp"
		    dependencies = ["com.google.guava:guava:+"]
            installTarget = "CHTX211"
			mainProgramName  = "com.apgsga.test.SomeMain"
			version = "1.0"
		    releaseNr = "1"
         }
        """

        when:
        def result = gradleRunnerFactory(['buildRpm']).build()
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


