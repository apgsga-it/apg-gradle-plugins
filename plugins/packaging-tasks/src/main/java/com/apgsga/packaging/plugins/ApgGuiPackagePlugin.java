package com.apgsga.packaging.plugins;

import com.apgsga.packaging.gui.actions.GuiZipPackageAction;
import com.apgsga.packaging.gui.actions.UnzipBundledResourcesToAction;
import com.apgsga.packaging.gui.tasks.BundledResourcesCopyTask;
import com.apgsga.packaging.gui.tasks.DllCopyTask;
import com.apgsga.packaging.gui.tasks.JarCopyTask;
import com.apgsga.packaging.gui.tasks.SerivceResourcesCopyTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Zip;

public class ApgGuiPackagePlugin implements Plugin<Project> {

    public static final String PLUGIN_ID = "com.apgsga.gui.package";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        final PluginContainer plugins = project.getPlugins();
        plugins.apply(ApgCommonPackagePlugin.class);
        TaskContainer tasks = project.getTasks();
        Task configureDepsTask = tasks.findByName("configureDeps");
        TaskProvider<DllCopyTask> dllCopyTask = tasks.register("dllCopyTask", DllCopyTask.class);
        TaskProvider<JarCopyTask> jarCopyTask = tasks.register("jarCopyTask", JarCopyTask.class);
        TaskProvider<SerivceResourcesCopyTask> resourcesCopyTask = tasks.register("resourcesCopyTask",
                SerivceResourcesCopyTask.class);
        TaskProvider<BundledResourcesCopyTask> bundledResourcesCopyTask = tasks.register("bundledResourcesCopyTask",
                BundledResourcesCopyTask.class);
        TaskProvider<Zip> zipPackageTask = tasks.register("buildZip", Zip.class, new GuiZipPackageAction(project));
        TaskProvider<Copy> unzipBundledResourcesTask = tasks.register("unzipBundledResourcesTask", Copy.class, new UnzipBundledResourcesToAction(project));
        dllCopyTask.configure(task -> task.dependsOn(configureDepsTask));
        jarCopyTask.configure(task -> task.dependsOn(configureDepsTask));
        bundledResourcesCopyTask.configure(task -> task.dependsOn(unzipBundledResourcesTask));
        resourcesCopyTask.configure(task -> task.dependsOn(dllCopyTask, jarCopyTask,bundledResourcesCopyTask));
        zipPackageTask.configure(task -> task.dependsOn(resourcesCopyTask));
    }
}
