package com.apgsga.ssh.rpm.tasks

import com.apgsga.ssh.extensions.ApgSshConfiguration
import com.apgsga.ssh.plugins.ApgSsh
import org.gradle.api.DefaultTask
import org.hidetake.groovy.ssh.core.Remote
import org.springframework.util.Assert

abstract class AbstractRpm extends DefaultTask {

    def getSshConfig() {
        return (ApgSshConfiguration) project.getExtensions().findByName(ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME)
    }

    def preConditions(def apgRpmDeployConfigExt, def apgSshCommonExt) {
        Assert.notNull(apgSshCommonExt.username, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a user name to be configured")
        Assert.notNull(apgSshCommonExt.userpwd, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a user password to be configured")
        Assert.notNull(apgSshCommonExt.destinationHost, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a destination host to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFilePath, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFilePath to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFileName, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFileName to be configured")
        Assert.notNull(apgRpmDeployConfigExt.remoteDestFolder, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDestFolder to be configured")
    }

    def getDeployConfig() {
        return project.extensions."${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME}"
    }

    def getRemotes() {
        def apgSshCommonExt = getSshConfig()
        def apgRpmDeployConfigExt = getDeployConfig()
        preConditions(apgRpmDeployConfigExt, apgSshCommonExt)
        // TODO JHE: in the future, do we want different pre-configured remote for our different target ?
        return new Remote(["name": "default", "host": "${apgSshCommonExt.destinationHost}", "user": "${apgSshCommonExt.username}", "password": "${apgSshCommonExt.userpwd}"]);
    }

    abstract def taskAction()

}
