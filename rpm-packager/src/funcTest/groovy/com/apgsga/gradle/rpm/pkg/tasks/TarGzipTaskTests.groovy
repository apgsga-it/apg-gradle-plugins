package com.apgsga.gradle.rpm.pkg.tasks

import static groovy.io.FileType.*
import static groovy.io.FileVisitResult.*
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class TarGzipTaskTests extends Specification {
	
    File testProjectDir
    File buildFile
	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packageing")
    } 
	
	def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile(); 
		println "Project Dir : ${testProjectDir.absolutePath}"
			new AntBuilder().copy(todir: "${testProjectDir}/packageing") {
			fileset(dir: resourcesDir)
		}
		buildFile = new File(testProjectDir,'build.gradle')
	}

    def "tarGzipAppPkg Task works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
            }
        apgRpmPackage {
			serviceName ="testapp"
			supportedServices = ["testapp"]
		    dependencies = ["com.apgsga.vk.digiflex:apg-vk-digiflex-web:9.1.0.DIGIFLEX-SNAPSHOT"]
            installTarget = "CHTX211"
			mainProgramName  = "com.apgsga.test.SomeMain"
         }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('tarGzipAppPkg','--info', '--stacktrace')
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

