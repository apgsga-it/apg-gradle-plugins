package com.apgsga.gradle.repo.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification


class BuildLogicFunctionalTest extends AbstractSpecification {

    String testMavenSettingsFilePath = new File("src/functionalTest/resources/testMavenSettings.xml").getAbsolutePath().replace("\\","/")

    def "Test build, publish locally and remote from a project having an external dependency by explicitly specifying a profile"() {
        given:
        buildFile << """
                                    plugins {
                                        id 'com.apgsga.common.repo' 
                                        id 'maven-publish'
                                    }
        
                                    apply plugin: 'groovy'
                                    apply plugin: 'java-gradle-plugin'
                                    
                                    group = 'com.apgsga.jhe'
                                    version = '1.0'
        
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