package com.apgsga.maven.dm.ext

import com.apgsga.gradle.test.utils.AbstractSpecification

class VersionResolutionDslTests extends AbstractSpecification {


    def "Version Resolution 1 Bom Version 1 Patch File DSL"() {
        given:
        buildFile << """
            plugins {
                id("com.apgsga.version.resolver")
            }
            versionResolvers {
                 boms = "aArtifactid:someGroupid:1:1-SNAPSHOT"
                 patches {
                   parentDir = "aParentDir"
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
              bomVersion = project.hasProperty('bomVersion') ? project.property('bomVersion') : "XXXXXXXX"
            }
            versionResolvers {
                 boms = "aArtifactid:someGroupid:\\\$\\\\{bomVersion\\\\}"
            }
        """

        when:
        def result = gradleRunnerFactory(['init','-PbomVersion=1234']).build()
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
              bomVersions = project.hasProperty('bomVersions') ? project.property('bomVersions') : "1-SNAPSHOT"
              patchParentDir = project.hasProperty('patchParentDir') ? project.property('patchParentDir') : "/"
              patchFiles = project.hasProperty('patchFiles') ? project.property('patchFiles') : "Patch8765.json:Patch8787.json"
            }
            versionResolvers {
                 boms = "\\\$\\\\{bomArtifactId\\\\}:\\\$\\\\{bomGroupId\\\\}:\\\$\\\\{bomVersions\\\\}"
                 patches {
                    parentDir = patchParentDir
                    fileNames = patchFiles
                 }
            }
        """

        when:
        def result = gradleRunnerFactory(['init','-PbomArtifactId=someBomArtifactId', '-PbomGroupId=someBomGroupId',
                                          '-PbomVersions=1234:9999:SNAPSHOT',
                                          '-PpatchParentDir=build/patches',
                                          '-PpatchFiles=Patch8765.json:Patch8787.json']).build()
        then:
        println "Result output: ${result.output}"
        result.output.contains('')
    }




}

