package com.apgsga.ssh.zip.tasks

import com.apgsga.ssh.extensions.ApgZipDeployConfig

class InstallZip extends AbstractZip {

    public static final String TASK_NAME = "installZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = getDeployConfig()
        if(!apgZipDeployConfigExt.zipFileName?.trim()) {
            project.logger.info("Latest ZIP within ${apgZipDeployConfigExt.remoteExtractDestFolder} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        } else {
            project.logger.info("${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")
        }
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
                if(!apgZipDeployConfigExt.zipFileName?.trim()) {
                    execute "f=\$(ls -t1 ${apgZipDeployConfigExt.remoteDeployDestFolder}/*.zip | head -n 1) && unzip \$f -d ${uiGettingExtractedFolder}"
                }
                else {
                    execute "unzip ${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName} -d ${uiGettingExtractedFolder}"
                }
                execute "chmod -R 775 ${uiGettingExtractedFolder}"
                execute "rm -f ${apgZipDeployConfigExt.remoteDeployDestFolder}/${apgZipDeployConfigExt.zipFileName}"
                execute "mv ${uiGettingExtractedFolder}/start_it21_gui_run.bat ${apgZipDeployConfigExt.remoteExtractDestFolder}"
                execute "mv ${uiGettingExtractedFolder} ${apgZipDeployConfigExt.remoteExtractDestFolder}/${newFolderName}"
                // JHE (11.06.2020): Keeping only last 3 version. Could be done with a script on platform as well if we want different behavior for PROD/TEST.
                execute "cd ${apgZipDeployConfigExt.remoteExtractDestFolder} && ls -rv | awk -F_ '++n[\$1]>3' | xargs rm -rf"
            }
        }
    }

    private def guiExtractedFolderName() {
        def currentDateAndTime = new Date().format('yyyyMMddHHmmss')
        def extractedFolderName = "java_gui_${currentDateAndTime}"
        return extractedFolderName
    }
}
