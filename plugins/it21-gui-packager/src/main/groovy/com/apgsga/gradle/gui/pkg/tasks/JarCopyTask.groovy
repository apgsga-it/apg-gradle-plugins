package com.apgsga.gradle.gui.pkg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class JarCopyTask extends DefaultTask { 
	
	// TODO (che, 2.10 ) : Verifiy if and when needed (in general)
	@OutputDirectory
    File getOutputDir() {
		def ex = project.extensions.apgPackage
		return new File("${project.buildDir}/${ex.pkgName}/lib") 
	}

	
	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		project.copy {
			into "${project.buildDir}/${ex.pkgName}/lib"
			from project.configurations.uiRuntime
			// In lib we only want jars files to go in lib folder (no dll, exe, bat, etc...)
			include "*.jar"
		}
		
	}
}
