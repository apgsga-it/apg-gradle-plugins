package com.apgsga.gradle.common.pkg.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class BinariesCopyTask extends DefaultTask { 
	
	// TODO (che, 2.10 ) : Verifiy if and when needed (in general)
	@OutputDirectory
    File getOutputDir() { return new File("${project.buildDir}/app-pkg/app/lib") }

	
	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		def configName = ex.configurationName
		// TODO (che, 1.10.19) : make excludes configurable via extension
		def config = project.configurations.findByName("${configName}")
		if (config == null) {
			config = project.configurations.create("${configName}")
		}
		project.dependencies {  
			ex.dependencies.each {
				"${configName}" it
			}
		}
		project.copy {
			into "${project.buildDir}/app-pkg/app/lib"
			from config
		}
		
	}
}
