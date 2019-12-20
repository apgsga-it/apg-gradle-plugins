package com.apgsga.gradle.ssh.plugin;

import com.apgsga.gradle.ssh.extensions.ApgSsh;
import com.apgsga.gradle.ssh.tasks.DeployRpm;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

@NonNullApi
public class ApgSshPlugin implements Plugin<Project> {

    public static final String APG_SSH_EXTENSION_NAME = "apgSsh";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        project.getExtensions().create(APG_SSH_EXTENSION_NAME, ApgSsh.class, project);
        project.getPlugins().apply("org.hidetake.ssh");
        TaskContainer tasks = project.getTasks();
        TaskProvider<DeployRpm> deployRpmTask = tasks.register(DeployRpm.DEPLOY_RPM_TASK_NAME,DeployRpm.class);
    }
}
