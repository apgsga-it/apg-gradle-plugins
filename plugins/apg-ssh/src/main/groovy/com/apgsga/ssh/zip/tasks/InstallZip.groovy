package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.extensions.ApgZipDeployConfig

class InstallZip extends AbstractZip {

    public static final String INTALL_ZIP_TASK_NAME = "installZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = getDeployConfig()
        project.logger.info("${apgZipDeployConfigExt.zipFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        def unzipCmd = getUnzipCmd(apgZipDeployConfigExt)
        project.ssh.run {
            if (apgZipDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remote) {
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
