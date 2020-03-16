package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.common.AbstractSshTask
import com.apgsga.ssh.plugins.ApgSsh
import org.springframework.util.Assert

abstract class AbstractZip extends AbstractSshTask {

    def preConditions() {
        def apgZipDeployConfigExt = getDeployConfig()
        Assert.notNull(apgZipDeployConfigExt.zipFilePath, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a zipFilePath to be configured")
        Assert.notNull(apgZipDeployConfigExt.zipFileName, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a zipFileName to be configured")
        Assert.notNull(apgZipDeployConfigExt.remoteDeployDestFolder, "${ApgSsh.APG_SSH_CONFIGURATION_EXTENSION_NAME} requires a remoteDeployDestFolder to be configured")
    }

    def getDeployConfig() {
        return project.extensions."${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME}"
    }
}
