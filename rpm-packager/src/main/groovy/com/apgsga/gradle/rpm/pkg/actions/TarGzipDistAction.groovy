package com.apgsga.gradle.rpm.pkg.actions

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.bundling.Compression
import org.gradle.api.tasks.bundling.Tar

import groovy.swing.impl.DefaultAction

class TarGzipDistAction implements Action<Tar> {
	
	Project project;

	public TarGzipDistAction(Project project) {
		this.project = project
	}

	@Override
	public void execute(Tar tar) {
		tar.from("${project.buildDir}/app-pkg")
		// TODO (che, 1.10 ) Verify 
		tar.destinationDir = new File("${project.buildDir}")
		tar.archiveName = "app-pkg.tar.gz"
		tar.compression = Compression.GZIP		
	}
}
