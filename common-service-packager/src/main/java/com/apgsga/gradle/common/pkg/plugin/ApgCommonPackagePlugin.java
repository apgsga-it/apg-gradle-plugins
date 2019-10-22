package com.apgsga.gradle.common.pkg.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

import com.apgsga.gradle.common.pkg.actions.CopyResourcesToBuildDirAction;
import com.apgsga.gradle.common.pkg.extension.ApgPackageExtension;
import com.apgsga.gradle.common.pkg.task.AppConfigFileMergerTask;
import com.apgsga.gradle.common.pkg.task.AppResourcesCopyTask;
import com.apgsga.gradle.common.pkg.task.BinariesCopyTask;
import com.apgsga.gradle.common.pkg.task.ResourceFileMergerTask;
import com.apgsga.gradle.common.pkg.task.TemplateDirCopyTask;
import com.apgsga.gradle.repo.config.plugin.ApgRepoConfigPlugin;

public class ApgCommonPackagePlugin implements Plugin<Project> {

	@SuppressWarnings("unused")
	@Override
	public void apply(Project project) {

		final ExtensionContainer ext = project.getExtensions();
		final Logger logger = project.getLogger();
		final PluginContainer plugins = project.getPlugins();
		plugins.apply(ApgRepoConfigPlugin.class);
		ext.create("apgPackage", ApgPackageExtension.class, project);
		TaskContainer tasks = project.getTasks();
		TaskProvider<Copy> copyPackagingResourcesTask = tasks.register("copyCommonPackagingResources", Copy.class,
				new CopyResourcesToBuildDirAction(project));
		TaskProvider<TemplateDirCopyTask> templateDirCopyTask = tasks.register("templateDirCopy",
				TemplateDirCopyTask.class);
		templateDirCopyTask.configure(task -> task.dependsOn(copyPackagingResourcesTask));
		TaskProvider<ResourceFileMergerTask> resourceMergeTask = tasks.register("mergeResourcePropertyFiles",
				ResourceFileMergerTask.class);
		TaskProvider<AppConfigFileMergerTask> appConfigMergeTask = tasks.register("mergeAppConfigPropertyFiles",
				AppConfigFileMergerTask.class);
		TaskProvider<AppResourcesCopyTask> appResourcesCopyAndExpandTask = tasks.register("copyAppResources",
				AppResourcesCopyTask.class);
		appResourcesCopyAndExpandTask
				.configure(task -> task.dependsOn(templateDirCopyTask, resourceMergeTask, appConfigMergeTask));
		TaskProvider<BinariesCopyTask> binariesCopyTask = tasks.register("copyAppBinaries", BinariesCopyTask.class);
		binariesCopyTask.configure(task -> task.dependsOn(appResourcesCopyAndExpandTask));

	}

}
