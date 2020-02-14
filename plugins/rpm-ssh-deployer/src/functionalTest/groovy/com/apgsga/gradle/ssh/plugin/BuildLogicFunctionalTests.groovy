package com.apgsga.gradle.ssh.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.springframework.core.io.ClassPathResource
import spock.lang.Ignore

// TODO (jhe, che, 14.2) These tests are because of che's reequest.
// TODO  But, need to be rethought on the background of integration tests with jenkins docker
// TODO They introduce a dependency to jenkins-t
@Ignore
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

    def "DeployRpm Tasks works against test environment for dummy RPM"() {
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
            def result = gradleRunnerFactory(['init','deployRpm']).build()
        then:
            result.output.toString().trim() ==~ /(?ms).*Started command.*apgGradlePluginDummyRpm-1-0.src.rpm.*/
            result.output.toString().trim() ==~ /(?ms).*Success command.*apgGradlePluginDummyRpm-1-0.src.rpm.*/
    }
}
