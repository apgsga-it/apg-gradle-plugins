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

class PropertyFileMergeTasksTests extends Specification {
	
    File testProjectDir
    File buildFile
	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packageing")
    } 
	
	def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile(); 
		println "Project Dir : ${testProjectDir.absolutePath}"
		new AntBuilder().copy(todir: "${testProjectDir}/build/packageing") {
			fileset(dir: resourcesDir)
		}
		buildFile = new File(testProjectDir,'build.gradle')
	}

    def "merge mergeResourcePropertyFiles Dir works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('mergeResourcePropertyFiles','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')	
		new File(testProjectDir,"build/template/app/conf/ops/resource.properties").exists()
		def dirTarget = new File(testProjectDir,"build/template")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		cntFiles == 1
		// TODO (che, 25.9) : test content of generated resources.properties
	
    }
	
	def "merge mergeResourcePropertyFiles with specific Service properties works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
            }
			apgRpmPackage {
				resourceFilters = "serviceport"
				appConfigFilters = "general"
				servicePropertiesDir = "resources"
			}
	    """
		def resourceDir = new File(testProjectDir,'resources')
		resourceDir.mkdirs()
		def resourccesProps = new File(resourceDir,'resource.properties')
		resourccesProps << """
			# Some service specific properties
			opsmessage=wtf
		"""

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('mergeResourcePropertyFiles','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
		def resultFile = new File(testProjectDir,"build/template/app/conf/ops/resource.properties")
		resultFile.exists()
		def dirTarget = new File(testProjectDir,"build/template")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		cntFiles == 1
		resultFile.text.contains("opsmessage=wtf")
		// TODO (che, 25.9) : test content of generated resources.properties
	
	}
	
	
	def "merge mergeAppConfigPropertyFiles Dir with specific Properties work works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
            }
			apgRpmPackage {
				resourceFilters = "serviceport"
				appConfigFilters = "general"
				servicePropertiesDir = "resources"
			}
	    """
		def resourceDir = new File(testProjectDir,'resources')
		resourceDir.mkdirs()
		def appConfigProperties = new File(resourceDir,'appconfig.properties')
		appConfigProperties << """
			# Some service specific properties
			message=hello
		"""
		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('mergeAppConfigPropertyFiles','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
		def resultFile = new File(testProjectDir,"build/template/app/conf/app/appconfig.properties")
		resultFile.exists()
		def dirTarget = new File(testProjectDir,"build/template")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		cntFiles == 1
		resultFile.text.contains("message=hello")
		// TODO (che, 25.9) : test content of generated appconfig.properties
	
	}
	
	def "merge mergeAppConfigPropertyFiles Dir works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.rpm.package' 
            }
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('mergeAppConfigPropertyFiles','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
		new File(testProjectDir,"build/template/app/conf/app/appconfig.properties").exists()
		def dirTarget = new File(testProjectDir,"build/template")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		cntFiles == 1
		// TODO (che, 25.9) : test content of generated appconfig.properties
	
	}

	
}


