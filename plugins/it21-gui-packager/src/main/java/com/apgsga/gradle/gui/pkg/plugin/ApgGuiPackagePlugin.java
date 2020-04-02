package com.apgsga.gradle.gui.pkg.plugin;

import com.apgsga.gradle.common.pkg.plugin.ApgCommonPackagePlugin;
import com.apgsga.gradle.common.pkg.task.ConfigureDepsTask;
import com.apgsga.gradle.gui.pkg.actions.UnzipBundledResourcesToAction;
import com.apgsga.gradle.gui.pkg.actions.ZipPackageAction;
import com.apgsga.gradle.gui.pkg.tasks.*;
import com.apgsga.gradle.repo.config.plugin.ApgRepoConfigPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Zip;

public class ApgGuiPackagePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		final PluginContainer plugins = project.getPlugins();
		plugins.apply(ApgRepoConfigPlugin.class);
		plugins.apply(ApgCommonPackagePlugin.class);
		TaskContainer tasks = project.getTasks();
		Task configureDepsTask = tasks.findByName("configureDeps");
		TaskProvider<DllCopyTask> dllCopyTask = tasks.register("dllCopyTask", DllCopyTask.class);
		TaskProvider<JarCopyTask> jarCopyTask = tasks.register("jarCopyTask", JarCopyTask.class);
		TaskProvider<SerivceResourcesCopyTask> resourcesCopyTask = tasks.register("resourcesCopyTask",
				SerivceResourcesCopyTask.class);
		TaskProvider<BundledResourcesCopyTask> bundledResourcesCopyTask = tasks.register("bundledResourcesCopyTask",
				BundledResourcesCopyTask.class); 
		TaskProvider<Zip> zipPackageTask = tasks.register("buildZip", Zip.class, new ZipPackageAction(project));
		TaskProvider<Copy> unzipBundledResourcesTask = tasks.register("unzipBundledResourcesTask", Copy.class, new UnzipBundledResourcesToAction(project));
		dllCopyTask.configure(task -> task.dependsOn(configureDepsTask));
		jarCopyTask.configure(task -> task.dependsOn(configureDepsTask));
		bundledResourcesCopyTask.configure(task -> task.dependsOn(unzipBundledResourcesTask));
		resourcesCopyTask.configure(task -> task.dependsOn(dllCopyTask, jarCopyTask,bundledResourcesCopyTask));
		zipPackageTask.configure(task -> task.dependsOn(resourcesCopyTask));
	}

}
