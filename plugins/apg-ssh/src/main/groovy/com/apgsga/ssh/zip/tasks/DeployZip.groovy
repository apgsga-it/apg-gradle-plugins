package com.apgsga.ssh.zip.tasks

class DeployZip extends AbstractZip {

    public static final String DEPLOY_ZIP_TASK_NAME = "deployZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = getDeployConfig()
        project.logger.info("${apgZipDeployConfigExt.zipFileName} will be deploy on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        project.ssh.run {
            if (apgZipDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remote) {
                put from: new File("${apgZipDeployConfigExt.zipFilePath}" + File.separator + apgZipDeployConfigExt.zipFileName), into: "${apgZipDeployConfigExt.remoteDeployDestFolder}"
                execute("ls -la ${apgZipDeployConfigExt.remoteDeployDestFolder}") { result ->
                    println result
                }
            }
        }
    }

}
