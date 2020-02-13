package com.apgsga.gradle.ssh.tasks

import org.gradle.api.tasks.TaskAction
import org.hidetake.groovy.ssh.core.Remote

class InstallRpm extends AbstractRpm {

    public static final String INSTALL_RPM_TASK_NAME = "installRpm"

    @TaskAction
    def taskAction() {
        def apgRpmDeployConfigExt = getDeployConfig()
        Remote remotes = getRemotes()
        project.logger.info("${apgRpmDeployConfigExt.rpmFileName} will be install on ${remotes.getProperty('host')} using ${remotes.getProperty('user')} User")
        project.ssh.run {
            if (apgRpmDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remotes) {
                executeSudo "rpm -Uvh ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}", pty: true
            }
        }
    }
}
