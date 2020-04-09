package com.apgsga.packaging.gui.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class BundledResourcesCopyTask extends DefaultTask { 
	
	// TODO (che, 2.10 ) : Verifiy if and when needed (in general)
	@OutputDirectory
    File getOutputDir() {
		def ex = project.extensions.apgPackage
		return new File("${project.buildDir}/${ex.name}")
	}

	
	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		project.copy {
			into "${project.buildDir}/${ex.name}"
			from ("${project.buildDir}/packaging") {
				expand(javaOpts:ex.javaOpts,mainProgramm:ex.mainProgramName,pkgName:ex.name)
				include '**/*.bat'
				include '**/logback.xml'
			}
			from ("${project.buildDir}/packaging") {
				exclude '**/*.bat'
				exclude '**/logback.xml'
			}
	
		 
		}
		
	}
}