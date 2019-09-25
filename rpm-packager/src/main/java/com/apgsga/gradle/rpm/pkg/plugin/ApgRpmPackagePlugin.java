package com.apgsga.gradle.rpm.pkg.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;

import com.apgsga.gradle.rpm.pkg.extension.ApgRpmPackageExtension;
import com.apgsga.gradle.rpm.pkg.tasks.ApgRpmPackageTask;
import com.apgsga.gradle.rpm.pkg.tasks.AppConfigFileMergerTask;
import com.apgsga.gradle.rpm.pkg.tasks.ResourceFileMergerTask;
import com.apgsga.gradle.rpm.pkg.tasks.RpmCopyScriptsTask;
import com.apgsga.gradle.rpm.pkg.tasks.TemplateDirCopyTask;

public class ApgRpmPackagePlugin  implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		final ExtensionContainer ext = project.getExtensions();
		final Logger logger = project.getLogger();
		final PluginContainer plugins = project.getPlugins();
		ext.create("apgRpmPackage", ApgRpmPackageExtension.class);
		TaskContainer tasks = project.getTasks();
		tasks.register("templateDirCopy", TemplateDirCopyTask.class);
		tasks.register("mergeResourcePropertyFiles", ResourceFileMergerTask.class);
		tasks.register("mergeAppConfigPropertyFiles", AppConfigFileMergerTask.class);
		tasks.register("copyRpmScripts", RpmCopyScriptsTask.class);
		tasks.register("apgRpmPackage", ApgRpmPackageTask.class);
		
	}

}
