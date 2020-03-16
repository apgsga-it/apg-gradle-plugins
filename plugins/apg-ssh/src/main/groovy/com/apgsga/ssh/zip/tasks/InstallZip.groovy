package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.extensions.ApgZipDeployConfig
import org.hidetake.groovy.ssh.core.Remote

class InstallZip extends AbstractZip {

    public static final String INTALL_ZIP_TASK_NAME = "installZip"

    @Override
    def taskAction() {
        def apgZipDeployConfigExt = getDeployConfig()
        Remote remotes = getRemotes()
        project.logger.info("${apgZipDeployConfigExt.zipFileName} will be install on ${remotes.getProperty('host')} using ${remotes.getProperty('user')} User")
        def unzipCmd = getUnzipCmd(apgZipDeployConfigExt)
        project.ssh.run {
            if (apgZipDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remotes) {
                executeSudo unzipCmd, pty: true
                // JHE: it probably won't sty like that, we might not want to delete ZIP which were built for production
                execute "rm -f ${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName}", pty: true
            }
        }
    }

    private def getUnzipCmd(ApgZipDeployConfig config) {
        def cmd = "unzip ${config.remoteDeployDestFolder}/${config.zipFileName}"
        if(config.remoteExtractDestFolder?.trim()) {
            cmd += " -d ${config.remoteExtractDestFolder}"
        }
        return cmd
    }
}
