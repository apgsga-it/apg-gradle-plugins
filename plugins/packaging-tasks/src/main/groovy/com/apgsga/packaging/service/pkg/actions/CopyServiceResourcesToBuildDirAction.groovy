package com.apgsga.packaging.service.pkg.actions

import com.apgsga.packaging.util.CopyUtil
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class CopyServiceResourcesToBuildDirAction implements Action<Copy> {
	
	Project project

	CopyServiceResourcesToBuildDirAction(Project project) {
		this.project = project
	}

	@Override
    void execute(Copy copy) {
		CopyUtil.copy(this.getClass(),project,copy)
	}
}
