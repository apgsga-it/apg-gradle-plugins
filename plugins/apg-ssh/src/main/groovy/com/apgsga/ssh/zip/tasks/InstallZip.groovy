package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.common.AbstractSshTask
import com.apgsga.ssh.common.Cmd
import com.apgsga.ssh.extensions.ApgZipDeployConfig
import com.apgsga.ssh.common.SshGenericTask
import com.apgsga.ssh.plugins.ApgSsh

class InstallZip extends AbstractSshTask {

    public static final String TASK_NAME = "installZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        def apgZipDeployConfigExt = project.extensions."${ApgSsh.APG_ZIP_DEPLOY_CONFIG_EXTENSION_NAME}"
        AbstractZipUtil.preConditions(apgZipDeployConfigExt)
        project.logger.info("${apgZipDeployConfigExt.zipFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")

        SshGenericTask sshTask = project.tasks.findByName(SshGenericTask.TASK_NAME)
        Cmd unzipCmd = Cmd.create().sshCmd(getUnzipCmd(apgZipDeployConfigExt)).isSudo(true)
        Cmd rmCmd = Cmd.create().sshCmd("rm -f ${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName}")
        sshTask.cmd = unzipCmd
        sshTask.doRun(remote,allowAnyHosts)
        sshTask.cmd = rmCmd
        sshTask.doRun(remote,allowAnyHosts)
    }

    private def getUnzipCmd(ApgZipDeployConfig config) {
        def cmd = "unzip ${config.remoteDeployDestFolder}/${config.zipFileName}"
        if(config.remoteExtractDestFolder?.trim()) {
            cmd += " -d ${config.remoteExtractDestFolder}"
        }
        return cmd
    }
}
