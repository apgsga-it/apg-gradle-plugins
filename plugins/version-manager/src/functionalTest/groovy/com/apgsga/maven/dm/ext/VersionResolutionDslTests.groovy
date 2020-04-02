package com.apgsga.maven.dm.ext

import com.apgsga.gradle.test.utils.AbstractSpecification

class VersionResolutionDslTests extends AbstractSpecification {


    def "Version Resolution 1 Bom Version 1 Patch File DSL"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
            }
            apgPackage {
               name = 'aserviceName'
               installTarget = 'CHEI212'
            }
            apgVersionResolver {
	            bomArtifactId = 'aArtifactid'
	            bomGroupId = 'someGroupid'
	            bomBaseVersion = '9.1.0.ADMIN-UIMIG'
	            revisionRootPath = 'aRevisionParentDir'
                patches {
                   parentDir = "aPatchParentDir"
                   fileNames = "Patch9999.json"
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
              bomBaseVersion = project.hasProperty('bomBaseVersion') ? project.property('bomBaseVersion') : "XXXXXXXX"
            }
           apgPackage {
               name = 'aserviceName'
               installTarget = 'CHEI212'
            }
             apgVersionResolver {
	            bomArtifactId = 'aArtifactid'
	            bomGroupId = 'someGroupid'
	            bomBaseVersion = "\\\$\\\\{bomBaseVersion\\\\}"
	            revisionRootPath = '.'
                patches {
                   parentDir = "aParentDir"
                   fileNames = "Patch9999.json"
                }
            }
        """

        when:
        def result = gradleRunnerFactory(['init','-PbomBaseVersion=1.1.0.XXXX-YYYY']).build()
        then:
        println "Result output: ${result.output}"
        result.output.contains('')
    }



    def "version Resolution DSL with List of boms and list of patch files"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
            }
            project.ext {
              bomArtifactId = project.hasProperty('bomArtifactId') ? project.property('bomArtifactId') : "aId"
              bomGroupId = project.hasProperty('bomGroupId') ? project.property('bomGroupId') : "aGroupd"
              bomBaseVersion = project.hasProperty('bomBaseVersion') ? project.property('bomBaseVersion') : "1"
              patchParentDir = project.hasProperty('patchParentDir') ? project.property('patchParentDir') : "/"
              patchFiles = project.hasProperty('patchFiles') ? project.property('patchFiles') : "Patch8765.json:Patch8787.json"
            }  
            apgPackage {
               name = 'aServiceName'
               installTarget = 'CHEI212'
            }
            apgVersionResolver {
	            bomArtifactId = "\\\$\\\\{bomArtifactId\\\\}"
	            bomGroupId = "\\\$\\\\{bomGroupId\\\\}"
	            bomBaseVersion = "\\\$\\\\{bomVersion\\\\}"
	            revisionRootPath = '.'
                patches {
                   parentDir = patchParentDir
                   fileNames = patchFiles
                }
            }

        """

        when:
        def result = gradleRunnerFactory(['init','-PbomArtifactId=someBomArtifactId', '-PbomGroupId=someBomGroupId',
                                          '-PbomBaseVersion=9.1.0.ADMIN-UIMIG',
                                          '-PpatchParentDir=build/patches',
                                          '-PpatchFiles=Patch8765.json:Patch8787.json']).build()
        then:
        println "Result output: ${result.output}"
        result.output.contains('')
    }




}

