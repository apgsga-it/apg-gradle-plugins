package com.apgsga.gradle.ssh.tasks

import com.apgsga.gradle.ssh.plugin.ApgRpmSshDeployer
import com.apgsga.ssh.common.extensions.ApgSshCommon
import com.apgsga.ssh.common.plugin.ApgSshCommonPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.hidetake.groovy.ssh.core.Remote
import org.springframework.util.Assert

class DeployRpm extends DefaultTask {

    public static final String DEPLOY_RPM_TASK_NAME = "deployRpm"

    @TaskAction
    def taskAction() {
        def apgRpmDeployConfigExt = project.extensions."${ApgRpmSshDeployer.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME}"

        def apgSshCommonExt = getSshConfig()

        preConditions(apgRpmDeployConfigExt, apgSshCommonExt)
        project.logger.info("${apgRpmDeployConfigExt.rpmFileName} will be deploy on ${apgSshCommonExt.destinationHost} using ${apgSshCommonExt.username} User")
        // TODO JHE: in the future, do we want different pre-configured remote for our different target ?
        Remote remotes = new Remote(["name": "default", "host": "${apgSshCommonExt.destinationHost}", "user": "${apgSshCommonExt.username}", "password": "${apgSshCommonExt.userpwd}"])
        project.ssh.run {
            if (apgRpmDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remotes) {
                put from: new File("${apgRpmDeployConfigExt.rpmFilePath}" + File.separator + apgRpmDeployConfigExt.rpmFileName), into: "${apgRpmDeployConfigExt.remoteDestFolder}"
                execute("ls -la ${apgRpmDeployConfigExt.remoteDestFolder}") { result ->
                    println result
                }
                executeSudo "rpm -Uvh ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}", pty: true
            }
        }
    }

    private def getSshConfig() {
        return (ApgSshCommon) project.getExtensions().findByName(ApgSshCommonPlugin.APG_SSH_COMMON_EXTENSION_NAME)
    }

    private def preConditions(def apgRpmDeployConfigExt, def apgSshCommonExt) {
        Assert.notNull(apgSshCommonExt.username, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a user name to be configured")
        Assert.notNull(apgSshCommonExt.userpwd, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a user password to be configured")
        Assert.notNull(apgSshCommonExt.destinationHost, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_PLUGIN_ID} requires a destination host to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFilePath, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFilePath to be configured")
        Assert.notNull(apgRpmDeployConfigExt.rpmFileName, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a rpmFileName to be configured")
        Assert.notNull(apgRpmDeployConfigExt.remoteDestFolder, "${ApgRpmSshDeployer.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME} requires a remoteDestFolder to be configured")
    }
}