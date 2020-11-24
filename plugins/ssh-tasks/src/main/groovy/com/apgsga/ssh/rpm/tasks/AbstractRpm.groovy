package com.apgsga.ssh.rpm.tasks

import com.apgsga.ssh.common.AbstractSshTask
import com.apgsga.ssh.extensions.ApgRpmDeployConfig
import com.apgsga.ssh.extensions.ApgSshConfiguration
import com.apgsga.ssh.plugins.ApgSsh
import org.springframework.util.Assert

abstract class AbstractRpm extends AbstractSshTask {

    private def getSshConfig() {
        return (ApgSshConfiguration) project.getExtensions().findByName(ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME)
    }

    def preConditions() {
        def apgSshConfig = getSshConfig()
        def apgRpmDeployConfigExt = getDeployConfig()
        Assert.notNull(apgSshConfig.username, "${ApgSsh.PLUGIN_ID} requires a user name to be configured")
        Assert.notNull(apgSshConfig.userpwd, "${ApgSsh.PLUGIN_ID} requires a user password to be configured")
        Assert.notNull(apgSshConfig.destinationHost, "${ApgSsh.PLUGIN_ID} requires a destination host to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFilePath, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFilePath to be configured")
        Assert.notNull(apgRpmDeployConfigExt.remoteDestFolder, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDestFolder to be configured")
    }

    private def getDeployConfig() {
        return (ApgRpmDeployConfig) project.extensions."${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME}"
    }
}