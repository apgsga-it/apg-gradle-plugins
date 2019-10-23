package com.apgsga.gradle.gui.pkg.tasks


import static groovy.io.FileType.*
import static groovy.io.FileVisitResult.*
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class ZipPackageTaskTests extends Specification {
	
    File testProjectDir
    File buildFile
	
	def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile(); 
		println "Project Dir : ${testProjectDir.absolutePath}"
		buildFile = new File(testProjectDir,'build.gradle')
	}

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
         }
		 apgPackage.log()
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('zipPackageTask','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
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
		new File(testProjectDir,"build/distributions/testuiapp.zip").exists()
    }
	
	
}


