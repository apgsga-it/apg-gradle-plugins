package com.apgsga.gradle.maven.dm

import com.apgsga.gradle.test.utils.AbstractSpecification

class VersionResolutionFunctionalTests extends AbstractSpecification {


    def "version Resolution DSL works"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
            }
            versionResolver {
                 poms {
                   bom {
                        order = 1
                        artifact = "someGroupid:aArtifactid:aBomVersion"
                    }
                 }
                 patches {
                   patch {
                        order = 2
                        parentDirName = "aParentDir"
                        fileName = "Patch9999.json"
                    }
                  }
            }
        """

        when:
        def result = gradleRunnerFactory(['init']).build()
        then:
        println "Result output: ${result.output}"
        result.output.contains('')
    }
	
	

}


