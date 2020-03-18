package com.apgsga.ssh.plugins;

import com.apgsga.ssh.common.SshGenericTask;
import com.apgsga.ssh.extensions.ApgRpmDeployConfig;
import com.apgsga.ssh.extensions.ApgSshConfiguration;
import com.apgsga.ssh.extensions.ApgZipDeployConfig;
import com.apgsga.ssh.general.tasks.SshGetTask;
import com.apgsga.ssh.general.tasks.SshPutTask;
import com.apgsga.ssh.rpm.tasks.DeployRpm;
import com.apgsga.ssh.rpm.tasks.InstallRpm;
import com.apgsga.ssh.zip.tasks.DeployZip;
import com.apgsga.ssh.zip.tasks.InstallZip;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;

public class ApgSsh implements Plugin<Project> {

    private Project project;

    public static final String PLUGIN_ID = "com.apgsga.ssh";

    public static final String APG_SSH_CONFIGURATION_EXTENSION_NAME = "apgSshConfig";

    public static final String APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME = "apgRpmDeployConfig";

    public static final String APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME = "apgZipDeployConfig";

    @Override
    public void apply(Project project) {

        this.project = project;

        // Common Config
        project.getExtensions().create(APG_SSH_CONFIGURATION_EXTENSION_NAME, ApgSshConfiguration.class, project);
        project.getPlugins().apply("org.hidetake.ssh");
        TaskContainer tasks = project.getTasks();

        // RPM
        project.getExtensions().create(APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME, ApgRpmDeployConfig.class, project);
        tasks.register(DeployRpm.TASK_NAME,DeployRpm.class);
        tasks.register(InstallRpm.TASK_NAME, InstallRpm.class);

        // ZIP
        project.getExtensions().create(APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME, ApgZipDeployConfig.class, project);
        tasks.register(DeployZip.TASK_NAME, DeployZip.class);
        tasks.register(InstallZip.TASK_NAME, InstallZip.class);

        // Put
        tasks.register(SshPutTask.TASK_NAME, SshPutTask.class);

        // Get
        tasks.register(SshGetTask.TASK_NAME, SshGetTask.class);

        // Generic
        tasks.register(SshGenericTask.TASK_NAME, SshGenericTask.class);
    }
}
