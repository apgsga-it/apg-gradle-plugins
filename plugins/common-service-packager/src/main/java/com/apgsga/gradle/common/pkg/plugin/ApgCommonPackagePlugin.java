package com.apgsga.gradle.common.pkg.plugin;

import com.apgsga.gradle.common.pkg.extension.ApgCommonPackageExtension;
import com.apgsga.gradle.common.pkg.task.BinariesCopyTask;
import com.apgsga.gradle.common.pkg.task.ConfigureDepsTask;
import com.apgsga.gradle.repo.config.plugin.ApgRepoConfigPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class ApgCommonPackagePlugin implements Plugin<Project> {

	private Project project;

	@Override
	public void apply(Project project) {
		this.project = project;
		final ExtensionContainer ext = project.getExtensions();
		final Logger logger = project.getLogger();
		final PluginContainer plugins = project.getPlugins();
		plugins.apply(ApgRepoConfigPlugin.class);
		ext.create("apgPackage", ApgCommonPackageExtension.class, project);
		TaskContainer tasks = project.getTasks();
		TaskProvider<BinariesCopyTask> binariesCopyTask = tasks.register("copyAppBinaries", BinariesCopyTask.class);
		TaskProvider<ConfigureDepsTask> configureDeps = tasks.register("configureDeps", ConfigureDepsTask.class);
		binariesCopyTask.configure(task -> task.dependsOn(configureDeps));

	}


}
