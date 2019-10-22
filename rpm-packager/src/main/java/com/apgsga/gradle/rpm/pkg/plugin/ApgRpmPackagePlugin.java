package com.apgsga.gradle.rpm.pkg.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

import com.apgsga.gradle.common.pkg.plugin.ApgPackagePlugin;
import com.apgsga.gradle.repo.config.plugin.ApgRepoConfigPlugin;
import com.apgsga.gradle.rpm.pkg.actions.CopyResourcesToBuildDirAction;
import com.apgsga.gradle.rpm.pkg.plugins.OsPackagingPlugin;
import com.apgsga.gradle.rpm.pkg.tasks.OsPackageConfigureTask;
import com.apgsga.gradle.rpm.pkg.tasks.RpmScriptsCopyTask;

public class ApgRpmPackagePlugin implements Plugin<Project> {

	@SuppressWarnings("unused")
	@Override
	public void apply(Project project) {

		final ExtensionContainer ext = project.getExtensions();
		final Logger logger = project.getLogger();
		final PluginContainer plugins = project.getPlugins();
		plugins.apply(ApgPackagePlugin.class);
		plugins.apply(OsPackagingPlugin.class);
		TaskContainer tasks = project.getTasks();
		TaskProvider<Copy> copyPackagingResourcesAction = tasks.register("copyRpmPackagingResources", Copy.class,
				new CopyResourcesToBuildDirAction(project));
		TaskProvider<RpmScriptsCopyTask> rpmCopyAndExpandTask = tasks.register("copyRpmScripts",
				RpmScriptsCopyTask.class);
		TaskProvider<OsPackageConfigureTask> osPackageConfigureTask = tasks.register("osPackageConfigure",
				OsPackageConfigureTask.class);
		Task buildRpmTask = tasks.findByName("buildRpm");
		Task copyCommonPkgResourcesTask = tasks.findByName("copyCommonPackagingResources"); 
		Task templateDirCopyTask = tasks.findByName("templateDirCopy"); 
		Task tarGzipDistTask = tasks.findByName("tarGzipAppPkg"); 
		Task copyRpmPkgResources = tasks.findByName("copyRpmPackagingResources"); 
		templateDirCopyTask.dependsOn(copyRpmPkgResources,copyCommonPkgResourcesTask);
		rpmCopyAndExpandTask.configure(task -> task.dependsOn(templateDirCopyTask));
		osPackageConfigureTask.configure(task -> task.dependsOn(tarGzipDistTask));
		buildRpmTask.dependsOn(osPackageConfigureTask, rpmCopyAndExpandTask);

	}

}
