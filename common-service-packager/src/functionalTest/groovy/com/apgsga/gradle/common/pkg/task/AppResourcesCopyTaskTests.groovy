package com.apgsga.gradle.common.pkg.task

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files

import static groovy.io.FileType.FILES

class AppResourcesCopyTaskTests extends AbstractSpecification {
	

	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packageing")
    }

    def "copyRpmScripts works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.common.package' 
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("-Dgradle.user.home=${gradleHomeDir}", 'copyAppResources','--info', '--stacktrace')
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
	
	def "copyRpmScripts with explicit configuration works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.common.package' 
            }
		 apgPackage {
			serviceName ="testapp"
			supportedServices = ["testapp"]
            installTarget = "CHTX211"
			mainProgramName  = "com.apgsga.test.SomeMain"
         }
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments("-Dgradle.user.home=${gradleHomeDir}", 'copyAppResources','--info', '--stacktrace')
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


