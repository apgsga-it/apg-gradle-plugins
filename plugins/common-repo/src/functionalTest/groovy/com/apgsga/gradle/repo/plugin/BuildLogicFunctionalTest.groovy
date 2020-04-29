package com.apgsga.gradle.repo.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification


class BuildLogicFunctionalTest extends AbstractSpecification {

    String testMavenSettingsFilePath = new File("src/functionalTest/resources/testMavenSettings.xml").getAbsolutePath().replace("\\","/")

    def "Test publish in local Maven repository works"() {
        given:
            buildFile << """
                                plugins {
                                    id 'com.apgsga.common.repo' 
                                }
    
                                apply plugin: 'groovy'
                                apply plugin: 'java-gradle-plugin'
                                
                                group = 'com.apgsga.jhe'
                                version = '1.0'
    
                                mavenSettings {
                                    userSettingsFileName = '${testMavenSettingsFilePath}'
                                }
                                
                                publishing {
                                    repositories {
                                        maven {
                                            name = 'local'
                                        }
                                    } 
                                }
                                                          
                            """
        when:
            def result = gradleRunnerFactory(['clean', 'publish']).build()
        then:
            println "Result output: ${result.output}"
    }

    def "Test publish on remote repository works"() {
        given:
            buildFile << """
                            plugins {
                                id 'com.apgsga.common.repo' 
                            }

                            apply plugin: 'groovy'
                            apply plugin: 'java-gradle-plugin'
                            
                            group = 'com.apgsga.jhe'
                            version = '1.0'

                            mavenSettings {
                                userSettingsFileName = '${testMavenSettingsFilePath}'
                            }
                            
                            publishing {
                                repositories {
                                   maven {
                                        name = 'gradle_maven_releases'
                                    }
                                } 
                             }                           
                            
                        """
        when:
            def result = gradleRunnerFactory(['clean', 'publish']).build()
        then:
            println "Result output: ${result.output}"
    }

    def "Test publish on local and remote repository works"() {
        given:
            buildFile << """
                                plugins {
                                    id 'com.apgsga.common.repo' 
                                }
    
                                apply plugin: 'groovy'
                                apply plugin: 'java-gradle-plugin'
                                
                                group = 'com.apgsga.jhe'
                                version = '1.0'
    
                                mavenSettings {
                                    userSettingsFileName = '${testMavenSettingsFilePath}'
                                }
                                
                                publishing {
                                    repositories {
                                       maven {
                                            name = 'gradle_maven_releases'
                                       }
                                       maven {
                                            name = 'local'
                                       }
                                    } 
                                 }                           
                                
                            """
        when:
            def result = gradleRunnerFactory(['clean', 'publish']).build()
        then:
            println "Result output: ${result.output}"
    }

    def "Test build with a dependency coming from remote repository"() {
        given:
        buildFile << """
                                plugins {
                                    id 'com.apgsga.common.repo' 
                                }
    
                                apply plugin: 'groovy'
                                apply plugin: 'java-gradle-plugin'
                                
                                group = 'com.apgsga.jhe'
                                version = '1.0'
    
                                mavenSettings {
                                    userSettingsFileName = '${testMavenSettingsFilePath}'
                                }
                                
                                dependencies {
                                    compile group: 'com.google.guava', name: 'guava', version: '11.0.2'
                                }
                            """
        when:
            def result = gradleRunnerFactory(['clean', 'build']).build()
        then:
            println "Result output: ${result.output}"
    }

    def "Test build, publish locally and remote from a project having an external dependency"() {
        given:
            buildFile << """
                                    plugins {
                                        id 'com.apgsga.common.repo' 
                                    }
        
                                    apply plugin: 'groovy'
                                    apply plugin: 'java-gradle-plugin'
                                    
                                    group = 'com.apgsga.jhe'
                                    version = '1.0'
        
                                    mavenSettings {
                                        userSettingsFileName = '${testMavenSettingsFilePath}'
                                    }
                                    
                                    dependencies {
                                        compile group: 'com.google.guava', name: 'guava', version: '11.0.2'
                                    }
                                    
                                    publishing {
                                        repositories {
                                           maven {
                                                name = 'gradle_maven_releases'
                                           }
                                           maven {
                                                name = 'local'
                                           }
                                        } 
                                     }                                    
                                """
        when:
            def result = gradleRunnerFactory(['clean', 'build', 'publish']).build()
        then:
            println "Result output: ${result.output}"
    }
}