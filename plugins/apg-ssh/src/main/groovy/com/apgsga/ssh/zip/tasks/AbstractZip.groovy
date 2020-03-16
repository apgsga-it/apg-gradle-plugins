package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.extensions.ApgSshConfiguration
import com.apgsga.ssh.plugins.ApgSsh
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.hidetake.groovy.ssh.core.Remote
import org.springframework.util.Assert

abstract class AbstractZip extends DefaultTask {

    def getSshConfig() {
        return (ApgSshConfiguration) project.getExtensions().findByName(ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME)
    }

    def preConditions(def apgZipDeployConfigExt, def apgSshCommonExt) {
        Assert.notNull(apgSshCommonExt.username, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a user name to be configured")
        Assert.notNull(apgSshCommonExt.userpwd, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a user password to be configured")
        Assert.notNull(apgSshCommonExt.destinationHost, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a destination host to be configured")
        Assert.notNull(apgZipDeployConfigExt.zipFilePath, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a zipFilePath to be configured")
        Assert.notNull(apgZipDeployConfigExt.zipFileName, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a zipFileName to be configured")
        Assert.notNull(apgZipDeployConfigExt.remoteDeployDestFolder, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a remoteDeployDestFolder to be configured")
    }

    def getDeployConfig() {
        return project.extensions."${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME}"
    }

    def getRemotes() {
        def apgSshCommonExt = getSshConfig()
        def apgZipDeployConfigExt = getDeployConfig()
        preConditions(apgZipDeployConfigExt, apgSshCommonExt)
        // TODO JHE: in the future, do we want different pre-configured remote for our different target ?
        return new Remote(["name": "default", "host": "${apgSshCommonExt.destinationHost}", "user": "${apgSshCommonExt.username}", "password": "${apgSshCommonExt.userpwd}"]);
    }

    @TaskAction
    abstract def taskAction()
}
