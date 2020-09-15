package com.apgsga.ssh.zip.tasks

import com.apgsga.packaging.extensions.ApgCommonPackageExtension
import com.apgsga.ssh.general.tasks.SshPutTask

class DeployZip extends AbstractZip {

    public static final String TASK_NAME = "deployZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = getDeployConfig()
        def zipFileName = "${apgZipDeployConfigExt.zipFileParentPath}" + File.separator + getZipFileName()
        project.logger.info("${zipFileName} will be deploy on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        SshPutTask put = project.tasks.findByName(SshPutTask.TASK_NAME)
        put.from = new File(zipFileName)
        put.into = "${apgZipDeployConfigExt.remoteDeployDestFolder}"
        put.doRun(remote,allowAnyHosts)
    }
}