package com.apgsga.ssh.zip.tasks

class InstallZip extends AbstractZip {

    public static final String TASK_NAME = "installZip"

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = _getDeployConfig()
        def zipFileToBeExtracted = getZipFileToBeExtracted()
        project.logger.info("${zipFileToBeExtracted} will be install on ${remote.getProperty('host')} using ${remote.getProperty('user')} User")

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
                execute "sudo mkdir -p ${uiGettingExtractedFolder}"
                execute "sudo unzip ${zipFileToBeExtracted} -d ${uiGettingExtractedFolder}"
                execute "sudo mkdir ${uiGettingExtractedFolder}/log"
                execute "sudo chmod -R 775 ${uiGettingExtractedFolder}"
                execute "rm -f ${zipFileToBeExtracted}"
                execute "sudo mv ${uiGettingExtractedFolder}/start_it21_gui_run.bat ${apgZipDeployConfigExt.remoteExtractDestFolder}"
                execute "sudo mv ${uiGettingExtractedFolder} ${apgZipDeployConfigExt.remoteExtractDestFolder}/${newFolderName}"
                execute "sudo chgrp apg_install ${apgZipDeployConfigExt.remoteExtractDestFolder}/${newFolderName}"
                execute "sudo chown apg_install ${apgZipDeployConfigExt.remoteExtractDestFolder}/${newFolderName}"
                execute "cd ${apgZipDeployConfigExt.remoteExtractDestFolder} && ls -rv | awk -F_ '++n[\$1]>3' | xargs rm -rf"
            }
        }
    }

    private def guiExtractedFolderName() {
        def currentDateAndTime = new Date().format('yyyyMMddHHmmss')
        def extractedFolderName = "java_gui_${currentDateAndTime}"
        return extractedFolderName
    }

    private def getZipFileToBeExtracted() {
        def apgZipDeployConfigExt = _getDeployConfig()
        return "${apgZipDeployConfigExt.remoteDeployDestFolder}${File.separator}${_getZipFileName()}"
    }
}
