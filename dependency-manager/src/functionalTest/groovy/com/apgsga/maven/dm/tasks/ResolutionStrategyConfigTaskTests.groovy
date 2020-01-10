package com.apgsga.maven.dm.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.gradle.util.GFileUtils
import spock.lang.Shared

class ResolutionStrategyConfigTaskTests extends AbstractSpecification {

    private static final String REPO_URL = 'build/repo'

    private static final String TEST_REPO = 'bom-test'

    @Shared
    def source = new File("src/functionalTest/resources/patchresolvertests")

    def setup() {
        def source = new File("src/functionalTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
    }


    def "configureResolutionStrategy and dependencyResolution Bom Version with explicit Configuration "() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
                id 'java'
            }
            // Explicit creation of the configuration used
            configurations {
               jadasRuntime
            }
            apgRepos{
				config(MAVEN,[REPO_NAME:"${TEST_REPO}",REPO_BASE_URL:"${REPO_URL}"])	
			}
            versionResolvers {
                 configurationName = "jadasRuntime"
                 boms = "test:test-nested-bom:1.1"
            }
            dependencies {
               jadasRuntime 'org.apache.httpcomponents:httpclient'
            }
        """

        when:
        def result = gradleRunnerFactory(['configureResolutionStrategy']).build()
        then:
        println "Result output: ${result.output}"
        result.output.contains('')
    }

    def "configureResolutionStrategy and dependencyResolution Bom Version with implicit Configuration creation"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
                id 'java'
            }
        // Explicit creation of the configuration left out, doesn't work see expected Exception and comment
        //    configurations {
        //       jadasRuntime
        //    }
            apgRepos{
                config(MAVEN,[REPO_NAME:"${TEST_REPO}",REPO_BASE_URL:"${REPO_URL}"])	
			}
            versionResolvers {
                 configurationName = "jadasRuntime"
                 boms = "test:test-nested-bom:1.1"
            }
            dependencies {
               jadasRuntime 'org.apache.httpcomponents:httpclient'
            }
        """

        when:
        gradleRunnerFactory(['configureResolutionStrategy']).build()
        then:
        // TODO (che, 10.1) : dependencies eagarly expects the creation of non standard configrations. Needs to be discussed
        // TODO (che, 10.1) : Effectively the dependencies will be resolved lazyly in context for example rpm plugin
        UnexpectedBuildFailure ex = thrown()
        ex.message.contains("Could not find method jadasRuntime")

    }

    def "configureResolutionStrategy and dependencyResolution Bom Version with default Configuration creation"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
                id 'java'
            }
            apgRepos{
				config(MAVEN,[REPO_NAME:"${TEST_REPO}",REPO_BASE_URL:"${REPO_URL}"])	
			}
			// Implicit eager creation of default configuration serviceRuntime. works
            versionResolvers {
                 boms = "test:test-nested-bom:1.1"
            }
       
            dependencies {
               serviceRuntime 'org.apache.httpcomponents:httpclient'
            }
        """

        when:
        def result = gradleRunnerFactory(['configureResolutionStrategy']).build()
        then:
        println "Result output: ${result.output}"
        result.output.contains('')
    }

    def "configureResolutionStrategy and dependencyResolution Bom Versions and Patchfile with default Configuration creation"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
                id 'java'
            }
            apgRepos{
				config(MAVEN,[REPO_NAME:"${TEST_REPO}",REPO_BASE_URL:"${REPO_URL}"])	
			}
            versionResolvers {
                 boms = 'test:test-composite-bom:1.0'
                  patches {
                    parentDir = "${source.getAbsoluteFile()}"
                    fileNames = "PatchA5791.json"
                 }
            }
            dependencies {
               serviceRuntime "com.affichage.it21.vk:zentraldispo-dao"
            }
        """

        when:
        def result = gradleRunnerFactory(['configureResolutionStrategy']).build()
        then:
        // Will Resolve zentraldispo-dao via PatchA5791.json
        println "Result output: ${result.output}"
        result.output.contains('')
    }


}


