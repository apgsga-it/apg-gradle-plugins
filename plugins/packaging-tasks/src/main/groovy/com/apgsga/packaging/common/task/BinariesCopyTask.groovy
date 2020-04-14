package com.apgsga.packaging.common.task

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
		logger.info("Configure Dependency config ${configName}")
		def config = project.configurations.findByName("${configName}")
		project.copy {
			into "${project.buildDir}/app-pkg/app/lib"
			from config
		}
		
	}
}
