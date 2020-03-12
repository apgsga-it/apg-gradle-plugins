package com.apgsga.gradle.ssh.zip.tasks

import com.apgsga.gradle.ssh.zip.deployer.plugins.ApgZipSshDeployer
import com.apgsga.ssh.common.extensions.ApgSshCommon
import com.apgsga.ssh.common.plugin.ApgSshCommonPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.hidetake.groovy.ssh.core.Remote
import org.springframework.util.Assert

import java.util.function.Supplier

abstract class AbstractZip extends DefaultTask {

    def getSshConfig() {
        return (ApgSshCommon) project.getExtensions().findByName(ApgSshCommonPlugin.APG_SSH_COMMON_EXTENSION_NAME)
    }

    static def preConditions(def apgZipDeployConfigExt, def apgSshCommonExt) {
        Assert.notNull(apgSshCommonExt.username, "${ApgZipSshDeployer.APG_ZIP_DEPLOY_PLUGIN_ID} requires a user name to be configured")
        Assert.notNull(apgSshCommonExt.userpwd, "${ApgZipSshDeployer.APG_ZIP_DEPLOY_PLUGIN_ID} requires a user password to be configured")
        Assert.notNull(apgSshCommonExt.destinationHost, "${ApgZipSshDeployer.APG_ZIP_DEPLOY_PLUGIN_ID} requires a destination host to be configured")
        Assert.notNull(apgZipDeployConfigExt.zipFilePath, "${ApgZipSshDeployer.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME} requires a zipFilePath to be configured" as Supplier<java.lang.String>)
        Assert.notNull(apgZipDeployConfigExt.zipFileName, "${ApgZipSshDeployer.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME} requires a zipFileName to be configured" as Supplier<String>)
        Assert.notNull(apgZipDeployConfigExt.remoteDeployDestFolder, "${ApgZipSshDeployer.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDeployDestFolder to be configured" as Supplier<String>)
    }

    def getDeployConfig() {
        return project.extensions."${ApgZipSshDeployer.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME}"
    }

    def getRemotes() {
        def apgSshCommonExt = getSshConfig()
        def apgZipDeployConfigExt = getDeployConfig()
        preConditions(apgZipDeployConfigExt, apgSshCommonExt)
        // TODO JHE: in the future, do we want different pre-configured remote for our different target ?
        return new Remote(["name": "default", "host": "${apgSshCommonExt.destinationHost}", "user": "${apgSshCommonExt.username}", "password": "${apgSshCommonExt.userpwd}"])
    }

    @TaskAction
    abstract def taskAction()
}
