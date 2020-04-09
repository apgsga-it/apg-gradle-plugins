package com.apgsga.packaging.common.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgCommonPackagePlugin
import spock.lang.Shared

import static groovy.io.FileType.FILES

class BinariesCopyTaskTests extends AbstractSpecification {
	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packaging")
    } 

    def "copyAppBinaries creating confiug works"() {
        given:
        buildFile << """

            plugins {
                id '${ApgCommonPackagePlugin.PLUGIN_ID}'
            }

			apgRepositories {
				mavenLocal()
				mavenCentral()
			}

		// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
        apgPackage {
			name ="testapp"
			configurationName = 'serviceRuntime'
		    dependencies = ["com.google.guava:guava:+"]
            installTarget = "CHTX211"
			mainProgramName  = "com.apgsga.test.SomeMain"
         }
        """

        when:
        def result = gradleRunnerFactory(['copyAppBinaries']).build()
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
		cntFiles > 4
    }

	def "copyAppBinaries with config works"() {
		given:
		buildFile << """

            plugins {
                id '${ApgCommonPackagePlugin.PLUGIN_ID}'
            }
            
            configurations { testRuntime.exclude group: 'log4j', module: 'log4j' }

			apgRepositories {
				mavenLocal()
				mavenCentral()
			}

		// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
        apgPackage {
			name ="testapp"
			configurationName = 'testRuntime'
		    dependencies = ["com.google.guava:guava:+"]
            installTarget = "CHTX211"
			mainProgramName  = "com.apgsga.test.SomeMain"
         }
        """

		when:
		def result = gradleRunnerFactory(['copyAppBinaries']).build()
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
		cntFiles > 4

	}
	
	
}


