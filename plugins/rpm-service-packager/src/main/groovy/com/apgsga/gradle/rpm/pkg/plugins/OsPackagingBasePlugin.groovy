package com.apgsga.gradle.rpm.pkg.plugins

import com.netflix.gradle.plugins.packaging.ProjectPackagingExtension
import com.netflix.gradle.plugins.rpm.RpmPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.ConventionMapping
import org.gradle.api.internal.IConventionAware

class OsPackagingBasePlugin implements Plugin<Project> {


	Project project
	ProjectPackagingExtension extension

	public static final String taskBaseName = 'ospackage'

	void apply(Project project) {

		this.project = project

		// Extension is created before plugins are, so tasks
		extension = createExtension()
		RpmPlugin.applyAliases(extension) // RPM Specific aliases


		project.plugins.apply(RpmPlugin.class)
	}

	ProjectPackagingExtension createExtension() {
		ProjectPackagingExtension extension = project.extensions.create(taskBaseName, ProjectPackagingExtension, project)

		// Ensure extension is IConventionAware
		ConventionMapping mapping = ((IConventionAware) extension).getConventionMapping()

		return extension
	}
}
