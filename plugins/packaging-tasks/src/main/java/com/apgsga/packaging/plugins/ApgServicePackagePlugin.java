package com.apgsga.packaging.plugins;

import com.apgsga.packaging.service.pkg.actions.CopyServiceResourcesToBuildDirAction;
import com.apgsga.packaging.service.pkg.task.AppConfigFileMergerTask;
import com.apgsga.packaging.service.pkg.task.AppResourcesCopyTask;
import com.apgsga.packaging.service.pkg.task.ResourceFileMergerTask;
import com.apgsga.packaging.service.pkg.task.TemplateDirCopyTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class ApgServicePackagePlugin implements Plugin<Project> {

    public static final String PLUGIN_ID = "com.apgsga.service.package";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        final ExtensionContainer ext = project.getExtensions();
        final Logger logger = project.getLogger();
        final PluginContainer plugins = project.getPlugins();
        plugins.apply(ApgCommonPackagePlugin.class);
        TaskContainer tasks = project.getTasks();
        TaskProvider<Copy> copyPackagingResourcesTask = tasks.register("copyCommonPackagingResources", Copy.class,
                new CopyServiceResourcesToBuildDirAction(project));
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
        Task binariesCopyTask = tasks.findByName("copyAppBinaries");
        binariesCopyTask.dependsOn(appResourcesCopyAndExpandTask);
    }
}
