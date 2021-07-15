package com.apgsga.packaging.it21gui.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgGuiPackagePlugin
import spock.lang.Ignore

import static groovy.io.FileType.FILES

class ZipPackageTaskTests extends AbstractSpecification  {
    def "archivePackage works"() {
        given:
        buildFile << """

            plugins {
                id '${ApgGuiPackagePlugin.PLUGIN_ID}'
				id 'maven-publish' 
            }
            
            apply from : "${gradleHomeDirPath.replace("\\","/")}/common/portnr.gradle"
            repositories {
			   maven {
					name = 'public-test'
			   }
			 }
			apgVersionResolver {
				configurationName = "testRuntime"
				serviceName = "testuiapp"
				installTarget = 'CHEI212'
				bomBaseVersion = '2.1'
			}
			// The guava dependency is only for testing purposes, consider to be likely found in mavenCentral()
			apgPackage {
				name ="testuiapp"
				configurationName = "testRuntime"
				dependencies = ["com.google.guava:guava:+"]
				installTarget = 'CHEI212'
			 }
			 apgPackage.log()
 			 
			 publishing {
			 	publications {
			 		mavenJava(MavenPublication) {
			 			artifact source: '/todo/path/to/file', extension: 'zip'
			 		}
			 	}
				repositories {
					maven {
						name = 'local'
					}
				}
			 } 
        """

        when:
        def result = gradleRunnerFactory(['buildZip']).build()
        then:
		println "Result output: ${result.output}" 
        result.output.contains('')	
		new File(testProjectDir,"build/testuiapp").exists()
		def dirTarget = new File(testProjectDir,"build/testuiapp")
		def cntFiles = 0
		def cntFileVisitor = {
			cntFiles++
		}
		dirTarget.traverse type: FILES , visit: cntFileVisitor
		// TODO (che, 25.9) : haha, test is really terrific
		cntFiles > 4
		// TODO (che, 25.9) : test expected files
		new File(testProjectDir,"build/distributions/apg-testuiapp-CHEI212-2.1-SNAPSHOT-1.zip").exists()
    }
	
	
}


