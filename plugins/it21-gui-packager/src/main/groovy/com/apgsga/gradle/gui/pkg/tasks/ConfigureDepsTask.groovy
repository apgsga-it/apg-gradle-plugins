package com.apgsga.gradle.gui.pkg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ConfigureDepsTask extends DefaultTask {
	
	
	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		def configName = ex.configurationName
		def config = project.configurations.findByName(configName)
		if(config == null) {
			config = project.configurations.create(configName)
		}
		project.configurations {
			config
		}

		project.dependencies {  
			ex.dependencies.each {
				"${configName}" it
			}
		}
		
	}
}
