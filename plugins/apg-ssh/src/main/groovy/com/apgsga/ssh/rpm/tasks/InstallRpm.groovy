package com.apgsga.ssh.rpm.tasks

import com.apgsga.ssh.common.SshGenericTask

class InstallRpm extends AbstractRpm {

    public static final String TASK_NAME = "installRpm"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgRpmDeployConfigExt = getDeployConfig()
        project.logger.info("${apgRpmDeployConfigExt.rpmFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")

        def sshTask = SshGenericTask.create()
                .project(project)
                .remote(remote)
                .allowAnyHost(apgRpmDeployConfigExt.allowAnyHosts)

        sshTask.isSudo(true)
                .sshCmd("rpm -Uvh ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}")
                .doRun()

        sshTask.isSudo(false)
                .sshCmd("rm -f ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}")
                .doRun()
    }
}
