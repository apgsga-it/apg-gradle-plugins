package com.apgsga.ssh.rpm.tasks

class DeployRpm extends AbstractRpm {

    public static final String DEPLOY_RPM_TASK_NAME = "deployRpm"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgRpmDeployConfigExt = getDeployConfig()
        project.logger.info("${apgRpmDeployConfigExt.rpmFileName} will be deploy on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        project.ssh.run {
            if (apgRpmDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remote) {
                put from: new File("${apgRpmDeployConfigExt.rpmFilePath}" + File.separator + apgRpmDeployConfigExt.rpmFileName), into: "${apgRpmDeployConfigExt.remoteDestFolder}"
                execute("ls -la ${apgRpmDeployConfigExt.remoteDestFolder}") { result ->
                    println result
                }
            }
        }
    }
}