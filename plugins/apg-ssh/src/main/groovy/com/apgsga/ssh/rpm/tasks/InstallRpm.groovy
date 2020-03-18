package com.apgsga.ssh.rpm.tasks

import com.apgsga.ssh.common.AbstractSshTask
import com.apgsga.ssh.common.Cmd
import com.apgsga.ssh.common.SshGenericTask
import com.apgsga.ssh.plugins.ApgSsh

class InstallRpm extends AbstractSshTask {

    public static final String TASK_NAME = "installRpm"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        def apgRpmDeployConfigExt = project.extensions."${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME}"
        AbstractRpmUtil.preConditions(apgRpmDeployConfigExt)
        project.logger.info("${apgRpmDeployConfigExt.rpmFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        SshGenericTask sshTask = project.tasks.findByName(SshGenericTask.TASK_NAME)
        Cmd rpmCmd = Cmd.create().sshCmd("rpm -Uvh ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}").isSudo(true)
        Cmd rmCmd = Cmd.create().sshCmd("rm -f ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}")

        sshTask.cmd = rpmCmd
        sshTask.doRun(remote,allowAnyHosts)
        sshTask.cmd = rmCmd
        sshTask.doRun(remote,allowAnyHosts)
    }
}