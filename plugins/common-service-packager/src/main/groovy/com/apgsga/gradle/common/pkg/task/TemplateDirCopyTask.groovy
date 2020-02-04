package com.apgsga.gradle.common.pkg.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class TemplateDirCopyTask extends DefaultTask { 
	
	@OutputDirectory
    File getOutputDir() { return new File("${project.buildDir}/template") }

	
	@TaskAction
	def taskAction() {
		project.copy {
			from "${project.buildDir}/packageing"
			into "${project.buildDir}/template"
			exclude '**/resources'
			exclude '**/appconfigs'
		}
		
	}
}
