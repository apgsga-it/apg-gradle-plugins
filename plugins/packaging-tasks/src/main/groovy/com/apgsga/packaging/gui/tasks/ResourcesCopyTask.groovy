package com.apgsga.packaging.gui.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class ResourcesCopyTask extends DefaultTask {

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
			from ("${project.projectDir}/${ex.resourcesPath}") {
				include '**/*'
			}

		}
		
	}
}
