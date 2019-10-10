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
import org.gradle.api.tasks.bundling.Tar;

import com.apgsga.gradle.rpm.pkg.actions.CopyResourcesToBuildDirAction;
import com.apgsga.gradle.rpm.pkg.actions.TarGzipDistAction;
import com.apgsga.gradle.rpm.pkg.extension.ApgRpmPackageExtension;
import com.apgsga.gradle.rpm.pkg.plugins.OsPackagingPlugin;
import com.apgsga.gradle.rpm.pkg.tasks.AppConfigFileMergerTask;
import com.apgsga.gradle.rpm.pkg.tasks.AppResourcesCopyTask;
import com.apgsga.gradle.rpm.pkg.tasks.BinariesCopyTask;
import com.apgsga.gradle.rpm.pkg.tasks.OsPackageConfigureTask;
import com.apgsga.gradle.rpm.pkg.tasks.ResourceFileMergerTask;
import com.apgsga.gradle.rpm.pkg.tasks.RpmScriptsCopyTask;
import com.apgsga.gradle.rpm.pkg.tasks.TemplateDirCopyTask;

public class ApgRpmPackagePlugin implements Plugin<Project> {

	@SuppressWarnings("unused")
	@Override
	public void apply(Project project) {

		final ExtensionContainer ext = project.getExtensions();
		final Logger logger = project.getLogger();
		final PluginContainer plugins = project.getPlugins();
		plugins.apply(OsPackagingPlugin.class);
		ext.create("apgRpmPackage", ApgRpmPackageExtension.class, project);
		TaskContainer tasks = project.getTasks();
		TaskProvider<Copy> copyPackagingResourcesTask = tasks.register("copyPackagingResources", Copy.class,
				new CopyResourcesToBuildDirAction(project));
		TaskProvider<TemplateDirCopyTask> templateDirCopyTask = tasks.register("templateDirCopy",
				TemplateDirCopyTask.class);
		templateDirCopyTask.configure(task -> task.dependsOn(copyPackagingResourcesTask));
		TaskProvider<ResourceFileMergerTask> resourceMergeTask = tasks.register("mergeResourcePropertyFiles",
				ResourceFileMergerTask.class);
		TaskProvider<AppConfigFileMergerTask> appConfigMergeTask = tasks.register("mergeAppConfigPropertyFiles",
				AppConfigFileMergerTask.class);
		TaskProvider<RpmScriptsCopyTask> rpmCopyAndExpandTask = tasks.register("copyRpmScripts",
				RpmScriptsCopyTask.class);
		rpmCopyAndExpandTask.configure(task -> task.dependsOn(templateDirCopyTask));
		TaskProvider<AppResourcesCopyTask> appResourcesCopyAndExpandTask = tasks.register("copyAppResources",
				AppResourcesCopyTask.class);
		appResourcesCopyAndExpandTask
				.configure(task -> task.dependsOn(templateDirCopyTask, resourceMergeTask, appConfigMergeTask));
		TaskProvider<BinariesCopyTask> binariesCopyTask = tasks.register("copyAppBinaries", BinariesCopyTask.class);
		binariesCopyTask.configure(task -> task.dependsOn(appResourcesCopyAndExpandTask));
		TaskProvider<Tar> tarGzipDistTask = tasks.register("tarGzipAppPkg", Tar.class, new TarGzipDistAction(project));
		tarGzipDistTask.configure(task -> task.dependsOn(binariesCopyTask));
		TaskProvider<OsPackageConfigureTask> osPackageConfigureTask = tasks.register("osPackageConfigure",
				OsPackageConfigureTask.class);
		osPackageConfigureTask.configure(task -> task.dependsOn(tarGzipDistTask));
		Task buildRpmTask = tasks.findByName("buildRpm");
		buildRpmTask.dependsOn(osPackageConfigureTask, rpmCopyAndExpandTask);

	}

}
