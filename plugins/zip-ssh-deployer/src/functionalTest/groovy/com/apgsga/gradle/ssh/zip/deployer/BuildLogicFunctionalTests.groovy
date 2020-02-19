package com.apgsga.gradle.ssh.zip.deployer

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
                            id 'com.apgsga.zip.ssh.deployer' 
                        }
                        apgZipDeployConfig {
                            zipFilePath 'build/output'
                            zipFileName 'fakeZip.zip'
                            remoteDeployDestFolder '/etc/installer'
                            remoteExtractDestFolder '/bin/placeToBeInstalled'
                        }				
                        apgZipDeployConfig.log()
                    """
        when:
            def result = gradleRunnerFactory(['init']).build()
        then:
            println "Result output: ${result.output}"
            result.output.contains("zipFilePath='build/output'")
            result.output.contains("zipFileName='fakeZip.zip'")
            result.output.contains("remoteDeployDestFolder='/etc/installer'")
            result.output.contains("remoteExtractDestFolder='/bin/placeToBeInstalled'")
    }

    def "Deploy and install ZIP Tasks works against test environment for dummy ZIP with dest installation folder"() {
        given:
            def zipResource = new ClassPathResource("dummy.zip")
            def zipFileName = zipResource.getFilename()
            def zipParentFolder = zipResource.getURI().getPath() - zipFileName
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
                                id 'com.apgsga.zip.ssh.deployer'
                            }
    
                            apply plugin: 'nu.studer.credentials'
    
                            apgSshCommon {
                               username 'apg_install'
                               userpwd credentials.apg_install
                               destinationHost 'jenkins-t.apgsga.ch'
                            }
                            
                            apgZipDeployConfig {
                                zipFilePath '${zipParentFolder}'
                                zipFileName '${zipFileName}'
                                remoteDeployDestFolder '/home/apg_install'
                                remoteExtractDestFolder '/home/apg_install/targetFolderForInstallation'
                            }				
                        """
        when:
            def result = gradleRunnerFactory(['init','deployZip','installZip']).build()
        then:
            result.output.toString().trim() ==~ /(?ms).*Started command.*dummy.zip.*/
            result.output.toString().trim() ==~ /(?ms).*Success command.*dummy.zip.*/
    }

    def "Deploy and install ZIP Tasks works against test environment for dummy ZIP without dest installation folder"() {
        given:
            def zipResource = new ClassPathResource("dummy.zip")
            def zipFileName = zipResource.getFilename()
            def zipParentFolder = zipResource.getURI().getPath() - zipFileName
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
                                    id 'com.apgsga.zip.ssh.deployer'
                                }
        
                                apply plugin: 'nu.studer.credentials'
        
                                apgSshCommon {
                                   username 'apg_install'
                                   userpwd credentials.apg_install
                                   destinationHost 'jenkins-t.apgsga.ch'
                                }
                                
                                apgZipDeployConfig {
                                    zipFilePath '${zipParentFolder}'
                                    zipFileName '${zipFileName}'
                                    remoteDeployDestFolder '/home/apg_install'
                                }				
                            """
        when:
            def result = gradleRunnerFactory(['init','deployZip','installZip']).build()
        then:
            result.output.toString().trim() ==~ /(?ms).*Started command.*dummy.zip.*/
            result.output.toString().trim() ==~ /(?ms).*Success command.*dummy.zip.*/
    }
}
