package com.apgsga.ssh.rpm.tasks

import com.apgsga.ssh.general.tasks.SshPutTask

// JHE (17.03.2020): Not sure we want to keep this task, one could simply directly call SshPutTask.
//                   However, we might want to deployRpm somewhere else in the future? Might be good to keep this "wrapper"

class DeployRpm extends AbstractRpm {

    public static final String TASK_NAME = "deployRpm"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgRpmDeployConfigExt = getDeployConfig()
        SshPutTask put = project.tasks.findByName(SshPutTask.TASK_NAME)
        put.from = new File("${apgRpmDeployConfigExt.rpmFilePath}" + File.separator + apgRpmDeployConfigExt.rpmFileName)
        put.into = "${apgRpmDeployConfigExt.remoteDestFolder}"
        put.doRun(remote,allowAnyHosts)
    }
}