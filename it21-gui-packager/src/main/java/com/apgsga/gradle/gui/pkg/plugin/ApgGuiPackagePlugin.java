package com.apgsga.gradle.gui.pkg.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Zip;

import com.apgsga.gradle.gui.pkg.actions.ZipPackageAction;
import com.apgsga.gradle.gui.pkg.extension.ApgGuiPackageExtension;
import com.apgsga.gradle.gui.pkg.tasks.ConfigureDepsTask;
import com.apgsga.gradle.gui.pkg.tasks.DllCopyTask;
import com.apgsga.gradle.gui.pkg.tasks.JarCopyTask;
import com.apgsga.gradle.gui.pkg.tasks.ResourcesCopyTask;
import com.apgsga.gradle.repo.config.plugin.ApgRepoConfigPlugin;

public class ApgGuiPackagePlugin implements Plugin<Project> {

	@SuppressWarnings("unused")
	@Override
	public void apply(Project project) {

		final ExtensionContainer ext = project.getExtensions();
		final Logger logger = project.getLogger();
		final PluginContainer plugins = project.getPlugins();
		plugins.apply(ApgRepoConfigPlugin.class);
		ext.create("apgPackage", ApgGuiPackageExtension.class, project);
		TaskContainer tasks = project.getTasks();
		TaskProvider<ConfigureDepsTask> configureDepsTask = tasks.register("configureDepsTask",
				ConfigureDepsTask.class);
		TaskProvider<DllCopyTask> dllCopyTask = tasks.register("dllCopyTask", DllCopyTask.class);
		TaskProvider<JarCopyTask> jarCopyTask = tasks.register("jarCopyTask", JarCopyTask.class);
		TaskProvider<ResourcesCopyTask> resourcesCopyTask = tasks.register("resourcesCopyTask",
				ResourcesCopyTask.class);
		TaskProvider<Zip> zipPackageTask = tasks.register("zipPackageTask", Zip.class, new ZipPackageAction(project));
		dllCopyTask.configure(task -> task.dependsOn(configureDepsTask));
		jarCopyTask.configure(task -> task.dependsOn(configureDepsTask));
		resourcesCopyTask.configure(task -> task.dependsOn(dllCopyTask, jarCopyTask));
		zipPackageTask.configure(task -> task.dependsOn(resourcesCopyTask));
	}

}
