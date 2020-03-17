package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.extensions.ApgZipDeployConfig
import com.apgsga.ssh.common.SshGenericTask

class InstallZip extends AbstractZip {

    public static final String TASK_NAME = "installZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = getDeployConfig()
        project.logger.info("${apgZipDeployConfigExt.zipFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        def unzipCmd = getUnzipCmd(apgZipDeployConfigExt)

        def sshTask = SshGenericTask.create()
                .project(project)
                .remote(remote)
                .allowAnyHost(apgZipDeployConfigExt.allowAnyHosts)

        sshTask.isSudo(true)
                .sshCmd(unzipCmd)
                .doRun()

        sshTask.isSudo(false)
                .sshCmd("rm -f ${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName}")
                .doRun()
    }

    private def getUnzipCmd(ApgZipDeployConfig config) {
        def cmd = "unzip ${config.remoteDeployDestFolder}/${config.zipFileName}"
        if(config.remoteExtractDestFolder?.trim()) {
            cmd += " -d ${config.remoteExtractDestFolder}"
        }
        return cmd
    }
}
