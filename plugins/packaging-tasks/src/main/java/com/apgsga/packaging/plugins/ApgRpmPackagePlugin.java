package com.apgsga.packaging.plugins;

import com.apgsga.packaging.rpm.actions.CopyResourcesToBuildDirAction;
import com.apgsga.packaging.rpm.plugins.OsPackagingPlugin;
import com.apgsga.packaging.rpm.tasks.OsPackageConfigureTask;
import com.apgsga.packaging.rpm.tasks.RpmScriptsCopyTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class ApgRpmPackagePlugin implements Plugin<Project> {

    public static final String PLUGIN_ID = "com.apgsga.rpm.package";

    private Project project;

    @SuppressWarnings("unused")
    @Override
    public void apply(Project project) {
        final ExtensionContainer ext = project.getExtensions();
        final Logger logger = project.getLogger();
        final PluginContainer plugins = project.getPlugins();
        plugins.apply(ApgServicePackagePlugin.class);
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
        Task copyBinariesIntoLib = tasks.findByName("copyAppBinaries");
        Task copyRpmPkgResources = tasks.findByName("copyRpmPackagingResources");
        assert templateDirCopyTask != null;
        templateDirCopyTask.dependsOn(copyRpmPkgResources, copyCommonPkgResourcesTask);
        rpmCopyAndExpandTask.configure(task -> task.dependsOn(templateDirCopyTask));
        osPackageConfigureTask.configure(task -> task.dependsOn(copyBinariesIntoLib));
        assert buildRpmTask != null;
        buildRpmTask.dependsOn(osPackageConfigureTask, rpmCopyAndExpandTask);
    }
}
