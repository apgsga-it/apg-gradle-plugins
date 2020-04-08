package com.apgsga.packaging.rpm.actions

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
				include 'packaging/**'
			 })
            copy.into("${project.buildDir}")
        } else {
			copy.from(project.fileTree(loader.getResource("packaging")))
            copy.into("${project.buildDir}/packaging")
        }

	}
}
