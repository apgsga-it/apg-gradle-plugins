package com.apgsga.gradle.ssh.tasks

import com.apgsga.gradle.ssh.plugin.ApgSshPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.hidetake.groovy.ssh.core.Remote
import org.springframework.util.Assert

class DeployRpm extends DefaultTask {

    public static final String DEPLOY_RPM_TASK_NAME = "deployRpm";

    @TaskAction
    def taskAction() {
        def apgSshExtension = project.extensions."${ApgSshPlugin.APG_SSH_EXTENSION_NAME}"
        preConditions(apgSshExtension)
        project.logger.info("${apgSshExtension.rpmFileName} will be deploy on ${apgSshExtension.target} using ${apgSshExtension.username} User")
        // TODO JHE: in the future, do we want different pre-configured remote for our different target ?
        Remote remotes = new Remote(["name":"default", "host":"${apgSshExtension.target}", "user":"${apgSshExtension.username}", "password": "${apgSshExtension.userpassword}"])
        project.ssh.run {
               session(remotes) {
                   put from: new File("${apgSshExtension.rpmFilePath}" + File.separator + apgSshExtension.rpmFileName), into: "${apgSshExtension.remoteDestFolder}"
                   execute("ls -la ${apgSshExtension.remoteDestFolder}") { result ->
                       println result
                   }
                   executeSudo "rpm -Uvh ${apgSshExtension.remoteDestFolder}" + File.separator + apgSshExtension.rpmFileName, pty: true
               }
        }
    }

    private def preConditions(def apgSshExtension) {
        Assert.notNull(apgSshExtension.username,"${ApgSshPlugin.APG_SSH_EXTENSION_NAME} requires a username to be configured")
        Assert.notNull(apgSshExtension.userpassword, "${ApgSshPlugin.APG_SSH_EXTENSION_NAME} requires a userpassword to be configured")
        Assert.notNull(apgSshExtension.target, "${ApgSshPlugin.APG_SSH_EXTENSION_NAME} requires a target to be configured")
        Assert.notNull(apgSshExtension.rpmFilePath, "${ApgSshPlugin.APG_SSH_EXTENSION_NAME} requires a rpmFilePath to be configured")
        Assert.notNull(apgSshExtension.rpmFileName, "${ApgSshPlugin.APG_SSH_EXTENSION_NAME} requires a rpmFileName to be configured")
        Assert.notNull(apgSshExtension.remoteDestFolder, "${ApgSshPlugin.APG_SSH_EXTENSION_NAME} requires a remoteDestFolder to be configured")
    }
}