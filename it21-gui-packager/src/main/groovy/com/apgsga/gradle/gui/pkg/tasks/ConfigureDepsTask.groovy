package com.apgsga.gradle.gui.pkg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class ConfigureDepsTask extends DefaultTask { 
	
	
	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		// TODO (che, 1.10.19) : make excludes configurable via extension
		project.configurations {
			uiRuntime
		}

		project.dependencies {  
			ex.dependencies.each {  
				uiRuntime it
			}
		}
		
	}
}
