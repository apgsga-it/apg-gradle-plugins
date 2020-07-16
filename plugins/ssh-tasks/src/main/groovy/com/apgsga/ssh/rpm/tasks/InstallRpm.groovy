package com.apgsga.ssh.rpm.tasks

class InstallRpm extends AbstractRpm {

    public static final String TASK_NAME = "installRpm"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgRpmDeployConfigExt = getDeployConfig()
        if(!apgRpmDeployConfigExt.rpmFileName?.trim()) {
            project.logger.info("Latest RPM within ${apgRpmDeployConfigExt.remoteDestFolder} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        } else {
            project.logger.info("${apgRpmDeployConfigExt.rpmFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        }
        project.ssh.run {
            if (apgRpmDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }

            if(!apgRpmDeployConfigExt.rpmFileName?.trim()) {
                session(remote) {
                    execute "f=\$(ls -t1 ${apgRpmDeployConfigExt.remoteDestFolder}/*.rpm | head -n 1) && sudo rpm -Uvh \$f", pty: true
                    // JHE: probably we want to archive the RPM before deleting it ?? To be discussed with UGE/CHE
                    execute "f=\$(ls -t1 ${apgRpmDeployConfigExt.remoteDestFolder}/*.rpm | head -n 1) && rm -f \$f", pty: true
                }
            }
            else {
                session(remote) {
                    executeSudo "rpm -Uvh ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}", pty: true
                    // JHE: probably we want to archive the RPM before deleting it ?? To be discussed with UGE/CHE
                    execute "rm -f ${apgRpmDeployConfigExt.remoteDestFolder}/${apgRpmDeployConfigExt.rpmFileName}", pty: true
                }
            }
        }
    }
}
