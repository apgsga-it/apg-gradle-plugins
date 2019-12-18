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
                   aBom {
                        order = 1
                        artifact = "someGroupid:aArtifactid:aBomVersion"
                    }
                 }
                 patches {
                   somePatch {
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


    def "version Resolution DSL with Version Parameter and single Bom works"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
            }
            project.ext {
              bomVersion = project.hasProperty('bomVersion') ? project.property('bomVersion') : "XXXXXXXX"
            }
            versionResolver {
                 poms {
                   che211Bom {
                        artifact = "someGroupid:aArtifactid:" + bomVersion
                    }
                 }
            }
        """

        when:
        def result = gradleRunnerFactory(['init','-PbomVersion=1234']).build()
        then:
        println "Result output: ${result.output}"
        result.output.contains('')
    }


    def "version Resolution DSL with default Version Parameter and single Bom works"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
            }
            project.ext {
              bomVersion = project.hasProperty('bomVersion') ? project.property('bomVersion') : "XXXXXXXX"
            }
            versionResolver {
                 poms {
                   che211Bom {
                        artifact = "someGroupid:aArtifactid:" + bomVersion
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


