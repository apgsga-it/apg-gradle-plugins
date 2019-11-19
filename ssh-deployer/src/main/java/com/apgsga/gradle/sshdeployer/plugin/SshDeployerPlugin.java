package com.apgsga.gradle.sshdeployer.plugin;

import com.apgsga.gradle.sshdeployer.tasks.DeployRpm;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;

public class SshDeployerPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        final PluginContainer plugins = project.getPlugins();
        plugins.apply("org.hidetake.ssh");

        TaskContainer tasks = project.getTasks();
        tasks.register("deployRpm", DeployRpm.class);
    }
}
