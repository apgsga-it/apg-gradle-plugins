package com.apgsga.gradle.gui.pkg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class BundledResourcesCopyTask extends DefaultTask { 
	
	// TODO (che, 2.10 ) : Verifiy if and when needed (in general)
	@OutputDirectory
    File getOutputDir() {
		def ex = project.extensions.apgPackage
		return new File("${project.buildDir}/${ex.pkgName}") 
	}

	
	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		project.copy {
			into "${project.buildDir}/${ex.pkgName}"
			from ("${project.buildDir}/packageing") {
				expand(javaOpts:ex.javaOpts,mainProgramm:ex.mainProgramm,pkgName:ex.pkgName)
				include '**/*.bat'
				include '**/logback.xml'
			}
			from ("${project.buildDir}/packageing") {
				exclude '**/*.bat'
				exclude '**/logback.xml'
			}
	
		 
		}
		
	}
}
