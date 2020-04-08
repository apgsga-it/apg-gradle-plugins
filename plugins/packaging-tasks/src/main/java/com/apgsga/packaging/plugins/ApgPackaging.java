package com.apgsga.packaging.plugins;

import com.apgsga.gradle.repo.config.plugin.ApgRepoConfigPlugin;
import com.apgsga.packaging.common.task.BinariesCopyTask;
import com.apgsga.packaging.common.task.ConfigureDepsTask;
import com.apgsga.packaging.extensions.ApgCommonPackageExtension;
import com.apgsga.packaging.gui.actions.UnzipBundledResourcesToAction;
import com.apgsga.packaging.gui.actions.ZipPackageAction;
import com.apgsga.packaging.gui.tasks.BundledResourcesCopyTask;
import com.apgsga.packaging.gui.tasks.DllCopyTask;
import com.apgsga.packaging.gui.tasks.JarCopyTask;
import com.apgsga.packaging.gui.tasks.SerivceResourcesCopyTask;
import com.apgsga.packaging.service.pkg.actions.CopyResourcesToBuildDirAction;
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
import org.gradle.api.tasks.bundling.Zip;

public class ApgPackaging implements Plugin<Project> {

    public static final String PLUGIN_ID = "com.apgsga.packaging";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        applyCommonPlugin();
        applyServicePackagePlugin();
        applyGuiPackagingPlugin();
    }

    private void applyGuiPackagingPlugin() {
        final PluginContainer plugins = project.getPlugins();
        plugins.apply(ApgRepoConfigPlugin.class);
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

    private void applyCommonPlugin() {
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

    private void applyServicePackagePlugin() {
        final ExtensionContainer ext = project.getExtensions();
        final Logger logger = project.getLogger();
        final PluginContainer plugins = project.getPlugins();
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
        Task binariesCopyTask = tasks.findByName("copyAppBinaries");
        binariesCopyTask.dependsOn(appResourcesCopyAndExpandTask);
    }

}
