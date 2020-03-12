package com.apgsga.gradle.ssh.tasks

import com.apgsga.gradle.ssh.plugin.ApgRpmSshDeployer
import com.apgsga.ssh.common.extensions.ApgSshCommon
import com.apgsga.ssh.common.plugin.ApgSshCommonPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.hidetake.groovy.ssh.core.Remote
import org.hidetake.groovy.ssh.session.Session
import org.springframework.util.Assert

abstract class AbstractSshTask extends DefaultTask {

    @TaskAction
    def taskAction() {
        project.logger.info("Running ${this.toString()}")
        ApgSshCommon ext = project.getExtensions().findByName(ApgSshCommonPlugin.APG_SSH_COMMON_EXTENSION_NAME) as ApgSshCommon
        Assert.notNull(ext.username, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a user name to be configured")
        Assert.notNull(ext.userpwd, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a user password to be configured")
        Assert.notNull(ext.destinationHost, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a destination host to be configured")
        Remote remote = new Remote(["name": "default", "host": "${ext.destinationHost}", "user": "${ext.username}", "password": "${ext.userpwd}"])
        doRun(remote,ext.allowAnyHosts)
        project.logger.info("Running ${this.toString()} finished")
    }

    def abstract doRun(remote,allowAnyHosts)


}
