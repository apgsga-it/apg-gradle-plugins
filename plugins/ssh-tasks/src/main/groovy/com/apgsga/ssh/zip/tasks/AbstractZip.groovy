package com.apgsga.ssh.zip.tasks

import com.apgsga.packaging.extensions.ApgCommonPackageExtension
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
        def apgPkgCommon = getPkgCommonExt()
        Assert.notNull(apgSshConfig.username, "${ApgSsh.PLUGIN_ID} requires a user name to be configured")
        Assert.notNull(apgSshConfig.userpwd, "${ApgSsh.PLUGIN_ID} requires a user password to be configured")
        Assert.notNull(apgSshConfig.destinationHost, "${ApgSsh.PLUGIN_ID} requires a destination host to be configured")
        Assert.notNull(apgZipDeployConfigExt.zipFileParentPath, "${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME} requires a zipFileParentPath to be configured")
        Assert.notNull(apgZipDeployConfigExt.remoteDeployDestFolder, "${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDeployDestFolder to be configured")
        Assert.notNull(getZipfileName(), "ZipFileName cannot be null when deploying and installing a ZIP.")
    }

    def getDeployConfig() {
        return (ApgZipDeployConfig) project.extensions."${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME}"
    }

    def getPkgCommonExt() {
        return project.extensions.getByType(ApgCommonPackageExtension.class)
    }

    def getZipfileName() {
        return "${getPkgCommonExt().zipFileParentPath}" + File.separator + getDeployConfig().archiveName + ".zip"
    }
}
