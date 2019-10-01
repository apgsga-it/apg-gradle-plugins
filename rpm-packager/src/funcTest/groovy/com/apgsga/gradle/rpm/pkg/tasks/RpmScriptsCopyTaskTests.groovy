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

class RpmScriptsCopyTaskTests extends Specification {
	
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

    def "copyRpmScripts works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('copyRpmScripts','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')	
		new File(testProjectDir,"build/rpm").exists()
		def dirTarget = new File(testProjectDir,"build/rpm")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		cntFiles == 4
		// TODO (che, 25.9) : test expected files and content
	
    }
	
	
	
}

