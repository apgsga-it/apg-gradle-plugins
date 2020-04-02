package com.apgsga.gradle.common.pkg.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ConfigureDepsTask extends DefaultTask {
	
	
	@TaskAction
	def taskAction() {
		// TODO (che,jhe , 2.4.20) : Resolve Code duplication
		def ex = project.extensions.apgPackage
		def configName = ex.configurationName
		logger.info("Configure Dependency config ${configName}")
		def config = project.configurations.findByName("${configName}")
		if (config == null) {
			config = project.configurations.create("${configName}")
		}
		project.dependencies {  
			ex.dependencies.each {
				logger.info("Dependency with ${it} with config ${configName}")
				"${configName}" it
			}
		}
		
	}
}
