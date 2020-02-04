package com.apgsga.gradle.gui.pkg.actions

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class UnzipBundledResourcesToAction implements Action<Copy> {
	
	Project project

    private final ClassLoader loader = getClass().getClassLoader()

    UnzipBundledResourcesToAction(Project project) {
		this.project = project
	}

	@Override
    void execute(Copy copy) {
		if (getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm().endsWith("jar")) {
			copy.from(project.zipTree(getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm()).matching {
				include 'packageing/**'
			 })
            copy.into("${project.buildDir}")
        } else {
			copy.from(project.fileTree(loader.getResource("packageing")))
            copy.into("${project.buildDir}/packageing")
        }

	}
}
