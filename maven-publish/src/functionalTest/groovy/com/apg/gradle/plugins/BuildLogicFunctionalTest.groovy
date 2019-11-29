package com.apg.gradle.plugins

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner

class BuildLogicFunctionalTest extends AbstractSpecification {

	File sourcePath
	File propertyFile

    def setup() {
		sourcePath = new File(testProjectDir,'src/main/resources')
		println "Source  Dir : ${sourcePath.absolutePath}"
		
		sourcePath.mkdirs()
		propertyFile = new File(sourcePath,'application.properties')
		propertyFile.write  "First Line\n"
		propertyFile << "Second Line\n"
    } 

    def "publish to mavenRepoLocalTempDir works"() {
        given:
        buildFile << """
            plugins {
                 id 'com.apgsga.maven.publish' 
            }
			apgMavenPublish {
				local()
			} 
        """

        when:
        def result = gradleRunnerFactory(['clean','build', 'publish']).build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
    }

	def "publish to maven Remote works with Defaults"() {
		given:
		buildFile << """
            plugins {
                  id 'com.apgsga.maven.publish' 
            }
			apgMavenPublish {
				artefactId = "plugin-test"
				groupId = "com.apgsga.gradle.plugins.test"
		        artifactory()

			} 
        """

		when:
		def result = gradleRunnerFactory(['clean','build', 'publish']).build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}

	def "publish to both a maven Local and Remote Repo works with SNAPSHOT Version"() {
		given:	
		buildFile << """
            plugins {
                  id 'com.apgsga.maven.publish'  
            }
			apgMavenPublish {
				artefactId = "plugin-test"
				version = "1.0-SNAPSHOT"
				groupId = "com.apgsga.gradle.plugins.test"
				local()
		        artifactory()

			} 
        """

		when:
		def result = gradleRunnerFactory(['clean','build', 'publish']).build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}

	def "publish to both a maven Local and Remote Repo works with Release Version"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.maven.publish' 
            }
			apgMavenPublish {
				artefactId = "plugin-test"
				version = "1.0"
				groupId = "com.apgsga.gradle.plugins.test"
				local()
		        artifactory()

			} 
        """

		when:
		def result = gradleRunnerFactory(['clean','build', 'publish']).build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}

	def "publish to mavenLocal works"() {
		given:
		buildFile << """
            plugins {
                 id 'com.apgsga.maven.publish' 
            }
			apgMavenPublish {
				mavenLocal()
			} 
        """

		when:
		def result = gradleRunnerFactory(['clean','build', 'publish']).build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
}


