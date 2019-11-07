package com.apgsga.gradle.publish

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared

class BuildLogicFunctionalTest extends AbstractSpecification {

	@Shared File rpmToPublish
	@Shared File tarToPublish

    def setupSpec() {
		rpmToPublish = new File("src/functionalTest/resources/apg-plugintests-0.8.9-1.noarch.rpm")
		tarToPublish = new File("src/functionalTest/resources/app-plugintests-0.1.tar.gz")
    }

    def "publish rpm to local works"() {
        given:
        buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgGenericPublishConfig {
				artefactFile = file("${rpmToPublish.absolutePath}") 
				local()
			}
			apgGenericPublishConfig.log()
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('apgGenericPublish','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
    }
	
	
	def "publish rpm to remote works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgGenericPublishConfig {
				artefactFile = file("${rpmToPublish.absolutePath}")
				artifactory()
			}
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('apgGenericPublish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
	def "publish rpm to both local and remote works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgGenericPublishConfig {
				artefactFile = file("${rpmToPublish.absolutePath}")
				local()
				artifactory()
			}
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('apgGenericPublish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
	def "publish tarball to both local and remote works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgGenericPublishConfig {
				artefactFile = file("${tarToPublish.absolutePath}")
				local()
				artifactory()
			}
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('apgGenericPublish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
	def "publish tarball to local with explicit repo config"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.publish' 
            }
			apgLocalRepo {
				repoBaseUrl = "build"
				repoName = "anothertestrepo"
			}	
			apgLocalRepo.log()
			apgGenericPublishConfig {
				artefactFile = file("${tarToPublish.absolutePath}")
				local()
			}
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('apgGenericPublish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
}


