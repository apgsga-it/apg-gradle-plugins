package com.apgsga.ssh.rpm.tasks

class InstallRpm extends AbstractRpm {

    public static final String INSTALL_RPM_TASK_NAME = "installRpm"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgRpmDeployConfigExt = getDeployConfig()
        project.logger.info("${apgRpmDeployConfigExt.rpmFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        project.ssh.run {
            if (apgRpmDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remote) {
                executeSudo "rpm -Uvh ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}", pty: true
                // JHE: this probably won't stay like that, as we won't want to delete a production RPM before having archiving it. But quick fix for now for Digiflex
                execute "rm -f ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}", pty: true
            }
        }
    }
}
