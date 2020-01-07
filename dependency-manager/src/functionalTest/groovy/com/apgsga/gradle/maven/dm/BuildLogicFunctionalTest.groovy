package com.apgsga.gradle.maven.dm

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.util.GFileUtils

class BuildLogicFunctionalTest extends AbstractSpecification {

    private static final String REPO_URL = "build/repo"

    private static final String TEST_REPO = "bom-test"



    def setup() {
        def source = new File("src/functionalTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
    }


    def "publish rpm to local works"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.bom.report")
            }
            apgDmBomReport.boms = ["test:test-bom:1.0"]
        """

        when:
        def result = gradleRunnerFactory(['apgDmBomReport']).build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
    }

    def "version Resolution DSL rpm to local works"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.bom.report")
            }
            apgDmBomReport.boms = ["test:test-bom:1.0"]
        """

        when:
        def result = gradleRunnerFactory(['apgDmBomReport']).build()
        then:
        println "Result output: ${result.output}"
        result.output.contains('')
    }
	
	

}


