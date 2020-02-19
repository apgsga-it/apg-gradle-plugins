package com.apgsga.gradle.ssh.zip.tasks

import org.hidetake.groovy.ssh.core.Remote

class DeployZip extends AbstractZip {

    public static final String DEPLOY_ZIP_TASK_NAME = "deployZip"

    @Override
    def taskAction() {
        def apgZipDeployConfigExt = getDeployConfig()
        Remote remotes = getRemotes()
        project.logger.info("${apgZipDeployConfigExt.zipFileName} will be deploy on ${remotes.getProperty('host')} using ${remotes.getProperty('user')} User")
        project.ssh.run {
            if (apgZipDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remotes) {
                put from: new File("${apgZipDeployConfigExt.zipFilePath}" + File.separator + apgZipDeployConfigExt.zipFileName), into: "${apgZipDeployConfigExt.remoteDeployDestFolder}"
                execute("ls -la ${apgZipDeployConfigExt.remoteDeployDestFolder}") { result ->
                    println result
                }
            }
        }
    }
}
