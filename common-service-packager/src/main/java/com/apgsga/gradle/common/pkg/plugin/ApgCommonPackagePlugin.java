package com.apgsga.gradle.common.pkg.plugin;

import com.apgsga.gradle.common.pkg.actions.CopyResourcesToBuildDirAction;
import com.apgsga.gradle.common.pkg.extension.ApgCommonPackageExtension;
import com.apgsga.gradle.common.pkg.task.*;
import com.apgsga.gradle.repo.config.plugin.ApgRepoConfigPlugin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApgCommonPackagePlugin implements Plugin<Project> {

	private static final String INSTALLABLE_SERVICES_JSON_FILENAME = "supportedServices.json";

	private Project project;

	@SuppressWarnings("unused")
	@Override
	public void apply(Project project) {
		this.project = project;
		final ExtensionContainer ext = project.getExtensions();
		final Logger logger = project.getLogger();
		final PluginContainer plugins = project.getPlugins();
		plugins.apply(ApgRepoConfigPlugin.class);
		ApgCommonPackageExtension apgPackage = ext.create("apgPackage", ApgCommonPackageExtension.class, project);
		apgPackage.setSupportedServices(Stream.of(loadSupportedServices().supportedServices.split(",")).collect(Collectors.toList()));
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

	private SupportedServicesBean loadSupportedServices() {
		SupportedServicesBean ssb = null;
		try {
			ssb = new ObjectMapper().readerFor(SupportedServicesBean.class).readValue(getSupportedServicesAsResource().getFile());
		} catch (IOException e) {
			throw new RuntimeException("Problem while deserializing " + INSTALLABLE_SERVICES_JSON_FILENAME + ". Original   exception was: " + e.getMessage());
		}
		return ssb;
	}

	private Resource getSupportedServicesAsResource() {
		String gradleHome = project.getGradle().getGradleUserHomeDir().getAbsolutePath();
		FileSystemResourceLoader loader = new FileSystemResourceLoader();
		String installableServicesJsonFilePath = "file://" + gradleHome + File.separator + INSTALLABLE_SERVICES_JSON_FILENAME;
		Resource installableServicesJsonAsResource = loader.getResource(installableServicesJsonFilePath);
		Assert.isTrue(installableServicesJsonAsResource.exists(), "installableServices.json file not found! installableServicesJsonFilePath = " + installableServicesJsonFilePath);
		return installableServicesJsonAsResource;
	}

}
