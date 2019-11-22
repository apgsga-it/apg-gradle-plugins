package com.apgsga.gradle.gui.pkg.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner

import static groovy.io.FileType.FILES

class ZipPackageTaskTests extends AbstractSpecification  {

    def "archivePackage works"() {
        given:
        buildFile << """

            plugins {
                id 'com.apgsga.gui.package'
            }

			apgRepository {
				mavenLocal()
				mavenCentral()
			}

		// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
        apgPackage {
			pkgName ="testuiapp"
		    dependencies = ["com.google.guava:guava:+"]
			version = "2.1-SNAPSHOT"
         }
		 apgPackage.log()
        """

        when:
        def result = gradleRunnerFactory(['zipPackageTask','--info', '--stacktrace']).build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')	
		new File(testProjectDir,"build/testuiapp").exists()
		def dirTarget = new File(testProjectDir,"build/testuiapp")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		// TODO (che, 25.9) : haha, test is really terrific
		cntFiles > 4
		// TODO (che, 25.9) : test expected files
		new File(testProjectDir,"build/distributions/testuiapp-2.1-SNAPSHOT.zip").exists()
    }
	
	
}


