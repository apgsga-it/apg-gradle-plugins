package com.apgsga.ssh.common.plugin;

import com.apgsga.ssh.common.extensions.ApgSshCommon;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ApgSshCommonPlugin implements Plugin<Project> {

    public static final String APG_SSH_COMMON_EXTENSION_NAME = "apgSshCommon";

    public static final String APG_SSH_COMMON_PLUGIN_ID = "com.apgsga.ssh.common";

    @Override
    public void apply(Project project) {
        project.getExtensions().create(APG_SSH_COMMON_EXTENSION_NAME, ApgSshCommon.class, project);
    }
}
