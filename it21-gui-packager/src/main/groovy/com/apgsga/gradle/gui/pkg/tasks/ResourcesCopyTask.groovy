package com.apgsga.gradle.gui.pkg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class ResourcesCopyTask extends DefaultTask { 
	
	// TODO (che, 2.10 ) : Verifiy if and when needed (in general)
	@OutputDirectory
	public File getOutputDir() { 
		def ex = project.extensions.apgPackage
		return new File("${project.buildDir}/${ex.pkgName}") 
	}

	
	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		project.copy {
			into "${project.buildDir}/${ex.pkgName}"
			from "${project.projectDir}/${ex.resourcesPath}"

		}
		
	}
}
