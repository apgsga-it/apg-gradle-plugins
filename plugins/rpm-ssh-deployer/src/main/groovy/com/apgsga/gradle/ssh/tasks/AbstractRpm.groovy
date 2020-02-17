package com.apgsga.gradle.ssh.tasks

import com.apgsga.gradle.ssh.plugin.ApgRpmSshDeployer
import com.apgsga.ssh.common.extensions.ApgSshCommon
import com.apgsga.ssh.common.plugin.ApgSshCommonPlugin
import org.gradle.api.DefaultTask
import org.hidetake.groovy.ssh.core.Remote
import org.springframework.util.Assert

abstract class AbstractRpm extends DefaultTask {

    def getSshConfig() {
        return (ApgSshCommon) project.getExtensions().findByName(ApgSshCommonPlugin.APG_SSH_COMMON_EXTENSION_NAME)
    }

    def preConditions(def apgRpmDeployConfigExt, def apgSshCommonExt) {
        Assert.notNull(apgSshCommonExt.username, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a user name to be configured")
        Assert.notNull(apgSshCommonExt.userpwd, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a user password to be configured")
        Assert.notNull(apgSshCommonExt.destinationHost, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a destination host to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFilePath, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFilePath to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFileName, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFileName to be configured")
        Assert.notNull(apgRpmDeployConfigExt.remoteDestFolder, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDestFolder to be configured")
    }

    def getDeployConfig() {
        return project.extensions."${ApgRpmSshDeployer.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME}"
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
