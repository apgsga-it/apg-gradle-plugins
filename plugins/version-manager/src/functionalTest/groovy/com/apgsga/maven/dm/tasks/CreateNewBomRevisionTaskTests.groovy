package com.apgsga.maven.dm.tasks

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.util.GFileUtils
import spock.lang.Ignore
import spock.lang.Shared

class CreateNewBomRevisionTaskTests extends AbstractSpecification {

    private static final String REPO_URL = 'build/repo'

    private static final String TEST_REPO = 'bom-test'

    @Shared
    def sourcePath

    def setupSpec() {
    	String workingDirectory = System.getProperty("user.dir")
        println "WorkingDirectory: ${workingDirectory}"
        String fileSeperator = System.getProperty("file.separator")
        println "File Seperator ${fileSeperator}"
        sourcePath = "${workingDirectory}${fileSeperator}src${fileSeperator}functionalTest${fileSeperator}resources${fileSeperator}patchresolvertests"
        println "Sourcepath: ${sourcePath}"
    }
    
    def setup() {
        def source = new File("src/functionalTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
    }

  @Ignore
  //TODO (che, 12.3 ) Adopt this to new Publishing
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
                    parentDir = "${sourcePath.replace("\\", "\\\\")}"
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


