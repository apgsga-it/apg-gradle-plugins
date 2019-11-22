package com.apgsga.gradle.zip.pkg.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner

import java.nio.file.Files

import static groovy.io.FileType.FILES

class TarGzipTaskTests extends AbstractSpecification {

    def "tarGzipAppPkg Task works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.zip.package'
            }

			apgRepository {
				mavenLocal()
				mavenCentral()
			}

		// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
        apgPackage {
			serviceName ="testapp"
			supportedServices = ["testapp"]
		    dependencies = ["com.google.guava:guava:+"]
            installTarget = "CHTX211"
			mainProgramName  = "com.apgsga.test.SomeMain"
         }
        """

        when:
        def result = gradleRunnerFactory(['buildZipPkg','--info', '--stacktrace']).build()
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


