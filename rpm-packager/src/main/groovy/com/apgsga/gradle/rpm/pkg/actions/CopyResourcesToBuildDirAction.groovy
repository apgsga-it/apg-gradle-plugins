package com.apgsga.gradle.rpm.pkg.actions

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.bundling.Compression
import org.gradle.api.tasks.bundling.Tar

import groovy.swing.impl.DefaultAction

class CopyResourcesToBuildDirAction implements Action<Copy> {
	
	Project project;
	
	private final ClassLoader loader = getClass().getClassLoader();

	public CopyResourcesToBuildDirAction(Project project) {
		this.project = project
	}

	@Override
	public void execute(Copy copy) {
		if (getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm().endsWith("jar")) {
			copy.from(project.zipTree(getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm()).matching {
				include 'packageing/**'
			 });
		 	copy.into("${project.buildDir}");
		} else {
			copy.from(project.fileTree(loader.getResource("packageing")));
			copy.into("${project.buildDir}/packageing");
		}

	}
}
