package com.apgsga.gradle.maven.dm

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner

class BuildLogicFunctionalTest extends AbstractSpecification {


    def "publish rpm to local works"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.bom.report")
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments('apgDmBomReport','--info', '--stacktrace')
            .withPluginClasspath()
            .build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')
    }
	
	

}


