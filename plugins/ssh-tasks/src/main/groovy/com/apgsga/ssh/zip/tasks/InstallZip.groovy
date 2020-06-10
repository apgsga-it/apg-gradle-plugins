package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.extensions.ApgZipDeployConfig

class InstallZip extends AbstractZip {

    public static final String TASK_NAME = "installZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = getDeployConfig()
        project.logger.info("${apgZipDeployConfigExt.zipFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        def newFolderName = guiExtractedFolderName()
        project.ssh.run {
            if (apgZipDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remote) {
                def uiGettingExtractedFolder = "${apgZipDeployConfigExt.remoteExtractDestFolder}/gettingExtracted_${newFolderName}"
                execute "mkdir -p ${uiGettingExtractedFolder}"
                execute "unzip ${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName} -d ${uiGettingExtractedFolder}"
                execute "chmod -R 775 ${uiGettingExtractedFolder}"
                execute "rm -f ${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName}"
                execute "mv ${uiGettingExtractedFolder}/start_it21_gui_run.bat ${apgZipDeployConfigExt.remoteExtractDestFolder}"
                execute "mv ${uiGettingExtractedFolder} ${apgZipDeployConfigExt.remoteExtractDestFolder}/${newFolderName}"
            }
        }
    }

    private def guiExtractedFolderName() {
        def currentDateAndTime = new Date().format('yyyyMMddHHmmss')
        def extractedFolderName = "java_gui_${currentDateAndTime}"
        return extractedFolderName
    }
}
