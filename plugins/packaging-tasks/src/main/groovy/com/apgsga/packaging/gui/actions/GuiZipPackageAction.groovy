package com.apgsga.packaging.gui.actions

import com.apgsga.packaging.extensions.ApgCommonPackageExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip

class GuiZipPackageAction implements Action<Zip> {
	
	Project project

    GuiZipPackageAction(Project project) {
		this.project = project
	}

	@Override
    void execute(Zip zip) {
		def ex = project.extensions.getByType(ApgCommonPackageExtension.class)
		zip.from("${project.buildDir}/${ex.name}")
		// JHE (10.06.2020): Not 100% sure, but probably we'll want to ZIP without the parent root folder, basically only the content.
		//zip.into("${ex.name}")
		// TODO (che, 1.10 ) Verify 
		zip.destinationDir = new File("${project.buildDir}/distributions")
		// TODO (che, 23.10) : Is this enough for Version?
		zip.archiveName = "${ex.archiveName}.zip"
	}
}
