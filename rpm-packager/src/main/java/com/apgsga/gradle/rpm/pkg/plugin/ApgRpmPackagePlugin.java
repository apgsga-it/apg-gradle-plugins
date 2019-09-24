package com.apgsga.gradle.rpm.pkg.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;

import com.apgsga.gradle.rpm.pkg.extension.ApgRpmPackageExtension;
import com.apgsga.gradle.rpm.pkg.task.ApgRpmPackageTask;

public class ApgRpmPackagePlugin  implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		final ExtensionContainer ext = project.getExtensions();
		final Logger logger = project.getLogger();
		final PluginContainer plugins = project.getPlugins();
		ext.create("apgRpmPackage", ApgRpmPackageExtension.class, project);
		project.getTasks().create("apgRpmPackage", ApgRpmPackageTask.class);
		
	}

}
