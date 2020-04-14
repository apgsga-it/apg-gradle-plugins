package com.apgsga.packaging.zip.actions

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip

class ZipPackageAction implements Action<Zip> {
	
	Project project

    ZipPackageAction(Project project) {
		this.project = project
	}

	@Override
    void execute(Zip zip) {
		zip.from("${project.buildDir}/app-pkg")
		// TODO (che, 1.10 ) Verify 
		zip.destinationDir = new File("${project.buildDir}/distributions")
		zip.archiveName = "app-pkg.zip"
	}
}
