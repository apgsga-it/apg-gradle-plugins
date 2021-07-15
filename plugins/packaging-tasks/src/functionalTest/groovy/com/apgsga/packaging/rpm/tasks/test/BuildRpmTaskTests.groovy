package com.apgsga.packaging.rpm.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgRpmPackagePlugin
import spock.lang.Shared

import static groovy.io.FileType.FILES

class BuildRpmTaskTests extends AbstractSpecification {

	@Shared File resourcesDir

    def setupSpec() {
		resourcesDir = new File("src/main/resources/packaging")
    } 


    def "buildRpm works"() {
        given:
        buildFile << """
            plugins {
                id '${ApgRpmPackagePlugin.PLUGIN_ID}'
            }
            repositories {
			   maven {
					name = 'public-test'
			   }
			}
            
			apgVersionResolver {
				configurationName = "testRuntime"
					serviceName = "testapp"
					installTarget = 'CHTX211'
					bomBaseVersion = '1.0'
			}

		// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
			apgPackage {
				name ="testapp"
				dependencies = ["com.google.guava:guava:+"]
				installTarget = "CHTX211"
				mainProgramName  = "com.apgsga.test.SomeMain"
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

	def "buildRpm with Apg Maven derived Baseversion Number works"() {
		given:
		buildFile << """
            plugins {
                id '${ApgRpmPackagePlugin.PLUGIN_ID}'
            }
            repositories {
			 maven {
					name = 'public-test'
			  }
			}
			apgVersionResolver {
				configurationName = "testRuntime"
					serviceName = "someotherApp"
					installTarget = 'CHEI212'
					bomBaseVersion = '1.0.0.DEV-ADMIN-UIMIG'
			}

		// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
        apgPackage {
			name ="someotherApp"
		    dependencies = ["com.google.guava:guava:+"]
            installTarget = "CHEI212"
			mainProgramName  = "com.apgsga.test.SomeOtherMain"
		    releaseNr = "2"
         }
        """

		when:
		def result = gradleRunnerFactory(['buildRpm']).build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
		new File(testProjectDir,"build/distributions/apg-someotherApp-CHEI212-1.0.0.DEV~ADMIN~UIMIG~SNAPSHOT-2.noarch.rpm").exists()

	}
	
	
}


