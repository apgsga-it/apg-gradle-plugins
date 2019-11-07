package com.apgsga.gradle.common.pkg.task

import com.apgsga.gradle.test.utils.AbstractSpecification

import static groovy.io.FileType.*
import static groovy.io.FileVisitResult.*
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class BinariesCopyTaskTests extends AbstractSpecification {
	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packageing")
    } 

    def "copyAppBinaries works"() {
        given:
        buildFile << """

            plugins {
                id 'com.apgsga.common.package'
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
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('copyAppBinaries','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
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


