package com.apgsga.gradle.common.pkg.task

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared

import static groovy.io.FileType.FILES

class PropertyFileMergeTasksTests extends AbstractSpecification {
	

	@Shared File resourcesDir
	
    def setupSpec() {
		resourcesDir = new File("src/main/resources/packageing")
    }

    def "merge mergeResourcePropertyFiles Dir works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.common.package' 
            }
        """

        when:
        def result = gradleRunnerFactory(['mergeResourcePropertyFiles','--info', '--stacktrace']).build()
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
                id 'com.apgsga.common.package' 
            }
			apgPackage {
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
		def result = gradleRunnerFactory(['mergeResourcePropertyFiles','--info', '--stacktrace']).build()
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
                id 'com.apgsga.common.package' 
            }
			apgPackage {
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
		def result = gradleRunnerFactory(['mergeAppConfigPropertyFiles','--info', '--stacktrace']).build()
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
                id 'com.apgsga.common.package' 
            }
        """

		when:
		def result = gradleRunnerFactory(['mergeAppConfigPropertyFiles','--info', '--stacktrace']).build()
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


