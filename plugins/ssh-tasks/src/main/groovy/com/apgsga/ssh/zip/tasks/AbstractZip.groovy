package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.common.AbstractSshTask
import com.apgsga.ssh.extensions.ApgSshConfiguration
import com.apgsga.ssh.extensions.ApgZipDeployConfig
import com.apgsga.ssh.plugins.ApgSsh
import org.springframework.util.Assert

abstract class AbstractZip extends AbstractSshTask {

    def getSshConfig() {
        return (ApgSshConfiguration) project.getExtensions().findByName(ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME)
    }

    def preConditions() {
        def apgSshConfig = getSshConfig()
        def apgZipDeployConfigExt = getDeployConfig()
        Assert.notNull(apgSshConfig.username, "${ApgSsh.PLUGIN_ID} requires a user name to be configured")
        Assert.notNull(apgSshConfig.userpwd, "${ApgSsh.PLUGIN_ID} requires a user password to be configured")
        Assert.notNull(apgSshConfig.destinationHost, "${ApgSsh.PLUGIN_ID} requires a destination host to be configured")
        Assert.notNull(apgZipDeployConfigExt.zipFilePath, "${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME} requires a zipFilePath to be configured")
        Assert.notNull(apgZipDeployConfigExt.zipFileName, "${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME} requires a zipFileName to be configured")
        Assert.notNull(apgZipDeployConfigExt.remoteDeployDestFolder, "${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDeployDestFolder to be configured")
    }

    def getDeployConfig() {
        return (ApgZipDeployConfig) project.extensions."${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME}"
    }
}
