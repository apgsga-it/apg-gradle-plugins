package com.apgsga.packaging.plugins;

import com.apgsga.packaging.common.task.BinariesCopyTask;
import com.apgsga.packaging.common.task.ConfigureDepsTask;
import com.apgsga.packaging.extensions.ApgCommonPackageExtension;
import net.linguica.gradle.maven.settings.MavenSettingsPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class ApgCommonPackagePlugin implements Plugin<Project> {

    public static final String PLUGIN_ID = "com.apgsga.common.package";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        final ExtensionContainer ext = project.getExtensions();
        final Logger logger = project.getLogger();
        final PluginContainer plugins = project.getPlugins();
        plugins.apply(MavenSettingsPlugin.class);
        ext.create("apgPackage", ApgCommonPackageExtension.class, project);
        TaskContainer tasks = project.getTasks();
        TaskProvider<BinariesCopyTask> binariesCopyTask = tasks.register("copyAppBinaries", BinariesCopyTask.class);
        TaskProvider<ConfigureDepsTask> configureDeps = tasks.register("configureDeps", ConfigureDepsTask.class);
        binariesCopyTask.configure(task -> task.dependsOn(configureDeps));
    }
}
