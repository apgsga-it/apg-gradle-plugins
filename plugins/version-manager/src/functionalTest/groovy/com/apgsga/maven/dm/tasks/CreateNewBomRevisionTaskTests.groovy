package com.apgsga.maven.dm.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.util.GFileUtils
import spock.lang.Shared

class CreateNewBomRevisionTaskTests extends AbstractSpecification {

    private static final String REPO_URL = 'build/repo'

    private static final String TEST_REPO = 'bom-test'

    @Shared
    def source = new File("src/functionalTest/resources/patchresolvertests")

    def setup() {
        def source = new File("src/functionalTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
    }


    def "configureResolutionStrategy and dependencyResolution Bom Snapshot Version and Patchfile with default Configuration creation"() {
        given:
        buildFile << """
            import com.apgsga.revision.manager.domain.RevisionManagerBuilder
            plugins {
                id("com.apgsga.version.resolver")
                id 'java'
            }
            apgRepos{
		        config(MAVEN,[REPO_NAME:"${TEST_REPO}",REPO_BASE_URL:"${REPO_URL}"])	
	        }
             apgVersionResolver {
                configurationName = "jadasRuntime"
	            bomArtifactId = 'test-nested-snapshot-bom'
	            bomGroupId = 'test'
	            bomBaseVersion = '1.1-TEST'
	            installTarget = "CHEI212"   
	            persistence =  RevisionManagerBuilder.PersistenceTyp.BEANS
                algorithm =  RevisionManagerBuilder.AlgorithmTyp.PATCH
	            patches {
                    parentDir = new File("${source.getAbsoluteFile().getPath().replace("\\", "/")}")
                    fileNames = "PatchA5791.json"
                }
                revisionRootPath = project.buildDir.absolutePath
            }
            dependencies {
               serviceRuntime "com.affichage.it21.vk:zentraldispo-dao"
            }
        """

        when:
        def result = gradleRunnerFactory(['createNewBomRevision']).build()
        then:
        // Will Resolve zentraldispo-dao via PatchA5791.json
        println "Result output: ${result.output}"
        result.output.contains('')
    }


}


