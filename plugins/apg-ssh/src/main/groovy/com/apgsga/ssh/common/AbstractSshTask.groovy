package com.apgsga.ssh.common

import com.apgsga.ssh.extensions.ApgSshConfiguration
import com.apgsga.ssh.plugins.ApgSsh
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.hidetake.groovy.ssh.core.Remote
import org.springframework.util.Assert

abstract class AbstractSshTask extends DefaultTask {

    @TaskAction
    def taskAction() {
        project.logger.info("Running ${this.toString()}")
        ApgSshConfiguration ext = project.getExtensions().findByName(ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME)
        Assert.notNull(ext.username, "${ApgSsh.PLUGIN_ID} requires a user name to be configured")
        Assert.notNull(ext.userpwd, "${ApgSsh.PLUGIN_ID} requires a user password to be configured")
        Assert.notNull(ext.destinationHost, "${ApgSsh.PLUGIN_ID} requires a destination host to be configured")
        Remote remote = new Remote(["name": "default", "host": "${ext.destinationHost}", "user": "${ext.username}", "password": "${ext.userpwd}"]);
        doRun(remote,ext.allowAnyHosts)
        project.logger.info("Running ${this.toString()} finished")
    }

    def abstract doRun(remote,allowAnyHosts)


}
