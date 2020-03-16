package com.apgsga.ssh.rpm.tasks

import org.gradle.api.tasks.TaskAction
import org.hidetake.groovy.ssh.core.Remote

class DeployRpm extends AbstractRpm {

    public static final String DEPLOY_RPM_TASK_NAME = "deployRpm"

    @TaskAction
    def taskAction() {
        def apgRpmDeployConfigExt = getDeployConfig()
        Remote remotes = getRemotes()
        project.logger.info("${apgRpmDeployConfigExt.rpmFileName} will be deploy on ${remotes.getProperty('host')} using ${remotes.getProperty('user')} User")
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
            }
        }
    }
}