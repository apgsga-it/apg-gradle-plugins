package com.apgsga.ssh.zip

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.ssh.plugins.ApgSsh
import org.springframework.core.io.ClassPathResource
import spock.lang.Ignore

// TODO (jhe, che, 14.2) These tests are because of che's reequest.
// TODO  But, need to be rethought on the background of integration tests with jenkins docker
// TODO They introduce a dependency to jenkins-t
class BuildLogicZipFunctionalTests extends AbstractSpecification {

    def apgSshPluginId = ApgSsh.PLUGIN_ID

    def "Basic SSH configuration works"() {
        given:
            buildFile << """
                        plugins {
                            id '${apgSshPluginId}' 
                        }
                        apgZipDeployConfig {
                            zipFileParentPath 'build/output'
                            remoteDeployDestFolder '/etc/installer'
                            remoteExtractDestFolder '/bin/placeToBeInstalled'
                        }				
                        apgZipDeployConfig.log()
                    """
        when:
            def result = gradleRunnerFactory(['init']).build()
        then:
            println "Result output: ${result.output}"
            result.output.contains("zipFileParentPath='build/output'")
            result.output.contains("remoteDeployDestFolder='/etc/installer'")
            result.output.contains("remoteExtractDestFolder='/bin/placeToBeInstalled'")
    }

    @Ignore
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
                                id '${apgSshPluginId}'
                            }
    
                            apply plugin: 'nu.studer.credentials'
    
                            apgSshConfig {
                               username 'apg_install'
                               userpwd credentials.apg_install
                               destinationHost 'jenkins-t.apgsga.ch'
                            }
                            
                            apgZipDeployConfig {
                                zipFilePath '${zipParentFolder}'
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

    @Ignore
    def "Deploy and install ZIP Tasks works against test environment for dummy ZIP without dest installation folder"() {
        given:
            def zipResource = new ClassPathResource("testuiapp-2.1-SNAPSHOT.zip")
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
                                    id '${apgSshPluginId}'
                                }
        
                                apply plugin: 'nu.studer.credentials'
        
                                apgSshConfig {
                                   username 'apg_install'
                                   userpwd credentials.apg_install
                                   destinationHost 'jadas-t.apgsga.ch'
                                }
                                
                                apgZipDeployConfig {
                                    zipFilePath '${zipParentFolder}'
                                    remoteDeployDestFolder '/home/apg_install/downloads'
                                    remoteExtractDestFolder '/opt/digiflex_ui/testJHE'
                                }				
                            """
        when:
            def result = gradleRunnerFactory(['init' ,'deployZip', 'installZip']).build()
        then:
            result.output.toString().trim() ==~ /(?ms).*Started command.*dummy.zip.*/
            result.output.toString().trim() ==~ /(?ms).*Success command.*dummy.zip.*/
    }
}
