package com.apgsga.ssh.zip.tasks

class InstallZip extends AbstractZip {

    public static final String TASK_NAME = "installZip"

    public static final String APG_MAX_GUI_FOLDER_FOR_PROD = 'apg.max.gui.folder.for.prod'

    public static final String APG_MAX_GUI_FOLDER_FOR_NON_PROD = 'apg.max.gui.folder.for.non.prod'

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        preConditions()
        def apgZipDeployConfigExt = _getDeployConfig()
        def zipFileToBeExtracted = getZipFileToBeExtracted()
        def installationHost = remote.getProperty('host')
        project.logger.info("${zipFileToBeExtracted} will be install on ${installationHost} using ${remote.getProperty('user')} User")

        def newFolderName = guiExtractedFolderName()

        def maxGuiFolderToKept = installationHost.toLowerCase().contains('chpi211') ?
                                  project.hasProperty(APG_MAX_GUI_FOLDER_FOR_PROD) ? project.property(APG_MAX_GUI_FOLDER_FOR_PROD) : "10" :
                                  project.hasProperty(APG_MAX_GUI_FOLDER_FOR_NON_PROD) ? project.property(APG_MAX_GUI_FOLDER_FOR_NON_PROD) : "3"

        project.ssh.run {
            if (apgZipDeployConfigExt.allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remote) {
                def uiGettingExtractedFolder = "${apgZipDeployConfigExt.remoteExtractDestFolder}/gettingExtracted_${newFolderName}"
                execute "cd ${apgZipDeployConfigExt.remoteExtractDestFolder} && pwd | sudo chgrp apg_install \$(xargs)"
                execute "cd ${apgZipDeployConfigExt.remoteExtractDestFolder} && pwd | sudo chmod -R 775 \$(xargs)"
                execute "sudo mkdir -p ${uiGettingExtractedFolder}"
                execute "sudo unzip ${zipFileToBeExtracted} -d ${uiGettingExtractedFolder}"
                execute "sudo mkdir ${uiGettingExtractedFolder}/log"
                execute "sudo chmod -R 775 ${uiGettingExtractedFolder}"
                execute "sudo chmod 777 ${uiGettingExtractedFolder}/log"
                execute "rm -f ${zipFileToBeExtracted}"
                execute "sudo mv ${uiGettingExtractedFolder}/start_it21_gui_run.bat ${apgZipDeployConfigExt.remoteExtractDestFolder}"
                execute "sudo mv ${uiGettingExtractedFolder} ${apgZipDeployConfigExt.remoteExtractDestFolder}/${newFolderName}"
                execute "sudo chgrp -R apg_install ${apgZipDeployConfigExt.remoteExtractDestFolder}/${newFolderName}"
                execute "cd ${apgZipDeployConfigExt.remoteExtractDestFolder} && ls -rv | awk -F_ '++n[\$1]>${maxGuiFolderToKept}' | xargs rm -rf"
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
