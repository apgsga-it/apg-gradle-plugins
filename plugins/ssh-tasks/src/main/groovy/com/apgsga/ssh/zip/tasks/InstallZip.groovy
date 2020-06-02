package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.extensions.ApgZipDeployConfig

class InstallZip extends AbstractZip {

    public static final String TASK_NAME = "installZip"

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
                // TODO (jhe, che) : Real quick fix for Digiflex Problem, see IT-35824
               // executeSudo "rm -f ${apgZipDeployConfigExt.remoteExtractDestFolder}/digiflex-it21-ui/lib/*", pty: true
                executeSudo unzipCmd, pty: true
                // JHE: it probably won't sty like that, we might not want to delete ZIP which were built for production
                execute "rm -f ${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName}", pty: true
                executeSudo "chmod -R 755 ${apgZipDeployConfigExt.remoteExtractDestFolder}"
            }
        }
    }

    private def getUnzipCmd(ApgZipDeployConfig config) {
        // JHE_ -o option in order to override the dest files, if they exists.
        def cmd = "unzip -o ${config.remoteDeployDestFolder}/${config.zipFileName}"
        if(config.remoteExtractDestFolder?.trim()) {
            cmd += " -d ${config.remoteExtractDestFolder}"
        }
        return cmd
    }
}
