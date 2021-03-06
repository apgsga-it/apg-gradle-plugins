package com.apgsga.ssh.rpm.tasks

import com.apgsga.packaging.extensions.ApgCommonPackageExtension
import com.apgsga.ssh.general.tasks.SshPutTask
import com.apgsga.ssh.plugins.ApgSsh

// JHE (17.03.2020): Not sure we want to keep this task, one could simply directly call SshPutTask.
//                   However, we might want to deployRpm somewhere else in the future? Might be good to keep this "wrapper"

class DeployRpm extends AbstractRpm {

    public static final String TASK_NAME = "deployRpm"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        def apgRpmDeployConfigExt = project.extensions."${ApgSsh.APG_RPM_DEPLOY_CONFIG_EXTENSION_NAME}"
        def apgPkgCommon = project.extensions.getByType(ApgCommonPackageExtension.class)
        preConditions()
        SshPutTask put = project.tasks.findByName(SshPutTask.TASK_NAME)
        put.from = new File("${apgRpmDeployConfigExt.rpmFilePath}" + File.separator + apgPkgCommon.rpmArchiveName + ".noarch.rpm" )
        put.into = "${apgRpmDeployConfigExt.remoteDestFolder}"
        put.doRun(remote,allowAnyHosts)
    }
}