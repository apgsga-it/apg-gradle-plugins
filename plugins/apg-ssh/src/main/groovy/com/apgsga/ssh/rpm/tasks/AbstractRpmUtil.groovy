package com.apgsga.ssh.rpm.tasks

import com.apgsga.ssh.plugins.ApgSsh
import org.springframework.util.Assert

abstract class AbstractRpmUtil {

    static def preConditions(def apgRpmDeployConfigExt) {
        Assert.notNull(apgRpmDeployConfigExt.rpmFilePath, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFilePath to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFileName, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFileName to be configured")
        Assert.notNull(apgRpmDeployConfigExt.remoteDestFolder, "${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDestFolder to be configured")
    }
}