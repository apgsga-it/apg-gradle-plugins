package com.apgsga.gradle.gui.pkg.actions

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.bundling.Compression
import org.gradle.api.tasks.bundling.Tar
import org.gradle.api.tasks.bundling.Zip
import groovy.swing.impl.DefaultAction

class ZipPackageAction implements Action<Zip> {
	
	Project project;

	public ZipPackageAction(Project project) {
		this.project = project
	}

	@Override
	public void execute(Zip zip) {
		def ex = project.extensions.apgPackage
		zip.from("${project.buildDir}/${ex.pkgName}")
		zip.into("${ex.pkgName}")
		// TODO (che, 1.10 ) Verify 
		zip.destinationDir = new File("${project.buildDir}/distributions")
		// TODO (che, 23.10) : Is this enough for Version?
		zip.archiveName = "${ex.pkgName}-${ex.version}.zip"
	}
}
