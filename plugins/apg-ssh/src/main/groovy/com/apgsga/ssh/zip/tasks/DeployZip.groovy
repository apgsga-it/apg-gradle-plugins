package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.general.tasks.SshPutTask

// JHE (17.03.2020): Not sure we want to keep this task, one could simply directly call SshPutTask.
//                   However, we might want to deployRpm somewhere else in the future? Might be good to keep this "wrapper"

class DeployZip extends AbstractZip {

    public static final String TASK_NAME = "deployZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = getDeployConfig()
        project.logger.info("${apgZipDeployConfigExt.zipFileName} will be deploy on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        SshPutTask put = project.tasks.findByName(SshPutTask.TASK_NAME)
        put.from = new File("${apgZipDeployConfigExt.zipFilePath}" + File.separator + apgZipDeployConfigExt.zipFileName)
        put.into = "${apgZipDeployConfigExt.remoteDeployDestFolder}"
        put.doRun(remote,allowAnyHosts)
    }
}