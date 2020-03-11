package com.apgsga.gradle.ssh.plugin;

import com.apgsga.gradle.ssh.extensions.ApgRpmDeployConfig;
import com.apgsga.gradle.ssh.tasks.DeployRpm;
import com.apgsga.gradle.ssh.tasks.InstallRpm;
import com.apgsga.ssh.common.plugin.ApgSshCommonPlugin;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;

@NonNullApi
public class ApgRpmSshDeployer implements Plugin<Project> {

    public static final String APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME = "apgRpmDeployConfig";

    public static final String APG_RPM_DEPLOY_PLUGIN_ID = "com.apgsga.rpm.ssh.deployer";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        project.getExtensions().create(APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME, ApgRpmDeployConfig.class, project);
        project.getPlugins().apply("org.hidetake.ssh");
        project.getPlugins().apply(ApgSshCommonPlugin.APG_SSH_COMMON_PLUGIN_ID);
        TaskContainer tasks = project.getTasks();
        tasks.register(DeployRpm.DEPLOY_RPM_TASK_NAME,DeployRpm.class);
        tasks.register(InstallRpm.INSTALL_RPM_TASK_NAME,InstallRpm.class);
    }
}
