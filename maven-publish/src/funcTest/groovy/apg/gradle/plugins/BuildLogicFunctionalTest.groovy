package apg.gradle.plugins

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

import java.nio.file.Files
import java.nio.file.Path

class BuildLogicFunctionalTest extends Specification {
	
    File testProjectDir
    File buildFile
	File sourcePath
	File propertyFile

    def setup() {
		testProjectDir = Files.createTempDirectory('gradletestproject').toFile();
		println "Project Dir : ${testProjectDir.absolutePath}"
        buildFile = new File(testProjectDir,'build.gradle')
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
                id 'com.apgsga.mavenpublish' 
            }
			apgMavenPublish {
				localRepo {
				  configure()
                }
			} 
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('clean','build', 'publish','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
    }
	
	
	def "publish to maven Remote works"() {
		given:
		buildFile << """
            plugins {
                id 'com.apgsga.mavenpublish' 
            }
			apgMavenPublish {
		        remoteRepo {
			   		artefactId = "plugin-test"
					groupId = "com.apgsga.gradle.plugins.test"
					remoteRelease = "release-test"
			    	remoteSnapshot = "snapshots-test"
					configure()
                }

			} 
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('clean','build', 'publish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
	
	def "publish to both a maven Local and Remote Repo works"() {
		given:	
		buildFile << """
            plugins {
                id 'com.apgsga.mavenpublish' 
            }
			apgMavenPublish {
				localRepo {
				  configure()
                }
		        remoteRepo {
			   		artefactId = "plugin-test"
					groupId = "com.apgsga.gradle.plugins.test"
					remoteRelease = "release-test"
			    	remoteSnapshot = "snapshots-test"
					configure()
                }

			} 
        """

		when:
		def result = GradleRunner.create()
			.withProjectDir(testProjectDir)
			.withArguments('clean','build', 'publish','--info', '--stacktrace')
			.withPluginClasspath()
			.build()
		then:
		println "Result output: ${result.output}"
		result.output.contains('')
	}
}


