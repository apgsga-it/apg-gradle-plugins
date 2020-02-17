package com.apgsga.gradle.ssh.zip.deployer.plugins;

import com.apgsga.gradle.ssh.zip.deployer.extensions.ApgZipDeployConfig;
import com.apgsga.gradle.ssh.zip.tasks.DeployZip;
import com.apgsga.gradle.ssh.zip.tasks.InstallZip;
import com.apgsga.ssh.common.plugin.ApgSshCommonPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;

public class ApgZipSshDeployer implements Plugin<Project> {

    public static final String APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME = "apgZipDeployConfig";

    public static final String APG_ZIP_DEPLOY_PLUGIN_ID = "com.apgsga.zip.ssh.deployer";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        project.getExtensions().create(APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME, ApgZipDeployConfig.class, project);
        project.getPlugins().apply("org.hidetake.ssh");
        project.getPlugins().apply(ApgSshCommonPlugin.APG_SSH_COMMON_PLUGIN_ID);
        TaskContainer tasks = project.getTasks();
        tasks.register(DeployZip.DEPLOY_ZIP_TASK_NAME, DeployZip.class);
        tasks.register(InstallZip.INTALL_ZIP_TASK_NAME, InstallZip.class);
    }
}
