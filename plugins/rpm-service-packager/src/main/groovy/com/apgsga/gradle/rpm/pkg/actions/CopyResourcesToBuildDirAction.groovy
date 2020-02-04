package com.apgsga.gradle.rpm.pkg.actions

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class CopyResourcesToBuildDirAction implements Action<Copy> {
	
	Project project

    private final ClassLoader loader = getClass().getClassLoader()

    CopyResourcesToBuildDirAction(Project project) {
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
