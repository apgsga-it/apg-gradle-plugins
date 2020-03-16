package com.apgsga.ssh.rpm.tasks

import com.apgsga.ssh.common.AbstractSshTask
import com.apgsga.ssh.plugins.ApgSsh
import org.springframework.util.Assert

abstract class AbstractRpm extends AbstractSshTask {

    def preConditions() {
        def apgRpmDeployConfigExt = getDeployConfig()
        Assert.notNull(apgRpmDeployConfigExt.rpmFilePath, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFilePath to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFileName, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFileName to be configured")
        Assert.notNull(apgRpmDeployConfigExt.remoteDestFolder, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDestFolder to be configured")
    }

    def getDeployConfig() {
        return project.extensions."${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME}"
    }
}