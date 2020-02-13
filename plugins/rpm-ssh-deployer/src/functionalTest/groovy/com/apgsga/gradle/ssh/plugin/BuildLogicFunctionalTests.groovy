package com.apgsga.gradle.ssh.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.springframework.core.io.ClassPathResource

class BuildLogicFunctionalTests extends AbstractSpecification {

    def "Basic SSH configuration works"() {
        given:
            buildFile << """
                    plugins {
                        id 'com.apgsga.rpm.ssh.deployer' 
                    }
                    apgRpmDeployConfig {
                        rpmFilePath 'build/output'
                        rpmFileName 'fakeRpm.rpm'
                        remoteDestFolder '/etc/installer'
                    }				
                    apgRpmDeployConfig.log()
                """
        when:
            def result = gradleRunnerFactory(['init']).build()
        then:
            println "Result output: ${result.output}"
            result.output.contains("rpmFilePath='build/output'")
            result.output.contains("rpmFileName='fakeRpm.rpm'")
            result.output.contains("remoteDestFolder='/etc/installer'")
    }

    def "Deploy and Install Tasks works against test environment for dummy RPM"() {
        given:
            def rpmResource = new ClassPathResource("apgGradlePluginDummyRpm-1-0.src.rpm")
            def rpmFileName = rpmResource.getFilename()
            def rpmParentFolder = rpmResource.getURI().getPath() - rpmFileName
            buildFile << """
                        buildscript {
                            repositories {
                                mavenLocal()
                                mavenCentral()
                                jcenter()
                            }
                        
                            dependencies {
                                    classpath 'nu.studer:gradle-credentials-plugin:1.0.7'
                            }
                        }

                        plugins {
                            id 'com.apgsga.rpm.ssh.deployer'
                        }

                        apply plugin: 'nu.studer.credentials'

                        apgSshCommon {
                           username 'apg_install'
                           userpwd credentials.apg_install
                           destinationHost 'jenkins-t.apgsga.ch'
                        }
                        
                        apgRpmDeployConfig {
                            rpmFilePath '${rpmParentFolder}'
                            rpmFileName '${rpmFileName}'
                            remoteDestFolder '/home/apg_install'
                        }				
                    """
        when:
            def result = gradleRunnerFactory(['init','deployRpm', 'installRpm']).build()
        then:
            println result.output
            result.output.toString().trim() ==~ /(?ms).*Started command.*rpm -Uvh.*apgGradlePluginDummyRpm-1-0.src.rpm.*/
            result.output.toString().trim() ==~ /(?ms).*Success command.*rpm -Uvh.*apgGradlePluginDummyRpm-1-0.src.rpm.*/
    }
}
