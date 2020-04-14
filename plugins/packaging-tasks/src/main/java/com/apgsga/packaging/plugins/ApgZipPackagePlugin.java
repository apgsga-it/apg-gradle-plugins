package com.apgsga.packaging.plugins;

import com.apgsga.packaging.zip.actions.ZipPackageAction;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Zip;

public class ApgZipPackagePlugin implements Plugin<Project> {

    public static final String PLUGIN_ID = "com.apgsga.zip.package";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        final ExtensionContainer ext = project.getExtensions();
        final Logger logger = project.getLogger();
        final PluginContainer plugins = project.getPlugins();
        plugins.apply(ApgCommonPackagePlugin.class);
        TaskContainer tasks = project.getTasks();
        Task libsCopyTask = tasks.findByName("copyAppBinaries");
        TaskProvider<Zip> tarGzipDistTask = tasks.register("buildZipPkg", Zip.class, new ZipPackageAction(project));
        tarGzipDistTask.configure(task -> task.dependsOn(libsCopyTask));
    }
}
