package com.apgsga.gradle.rpm.pkg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class TemplateDirCopyTask extends DefaultTask { 
	
	@OutputDirectory
	public File getOutputDir() { return new File("${project.buildDir}/template") }

	
	@TaskAction
	def taskAction() {
		project.copy {
			from 'packageing'
			into "${project.buildDir}/template"
			exclude '**/resources'
			exclude '**/appconfigs'
		}
		
	}
}