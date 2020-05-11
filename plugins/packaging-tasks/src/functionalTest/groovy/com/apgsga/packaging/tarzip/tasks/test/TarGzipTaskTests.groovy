package com.apgsga.packaging.tarzip.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgZipPackagePlugin

import static groovy.io.FileType.FILES

class TarGzipTaskTests extends AbstractSpecification {

	String testMavenSettingsFilePath = new File("src/functionalTest/resources/testMavenSettings.xml").getAbsolutePath().replace("\\","/")

    def "tarGzipAppPkg Task works"() {
        given:
        buildFile << """
            plugins {
                id '${ApgZipPackagePlugin.PLUGIN_ID}'
            }

			mavenSettings {
              userSettingsFileName = '${testMavenSettingsFilePath}'
			  activeProfile = 'artifactory-test'
            }

		// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
        apgPackage {
			name ="testapp"
		    dependencies = ["com.google.guava:guava:+"]
            installTarget = "CHTX211"
			mainProgramName  = "com.apgsga.test.SomeMain"
         }
        """

        when:
        def result = gradleRunnerFactory(['buildZipPkg']).build()
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


