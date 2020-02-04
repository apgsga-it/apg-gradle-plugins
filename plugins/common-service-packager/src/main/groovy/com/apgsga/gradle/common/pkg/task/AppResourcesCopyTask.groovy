package com.apgsga.gradle.common.pkg.task

import org.apache.tools.ant.filters.FixCrLfFilter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class AppResourcesCopyTask extends DefaultTask {

	@OutputDirectory
    File getOutputDir() {
		return new File("${project.buildDir}/app-pkg/app")
	}


	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		project.copy {
			from "${project.buildDir}/template/app"
			into "$project.buildDir/app-pkg/app"
			include "**/*"
			rename { filename ->
				filename.replace 'servicetemplate', ex.targetServiceName
			}
			expand(serviceName:ex.serviceName,portNr:ex.portNr,installTarget:ex.installTarget,mainPrg:ex.mainProgramName,
				targetServiceName:ex.targetServiceName,targetServiceExecDir:ex.targetServiceExecDir,targetServiceDataDir:ex.targetServiceDataDir,
				targetServiceConfigDir:ex.targetServiceConfigDir,javaDir:ex.javaDir,javaDist:ex.javaDist,distRepoUrl:ex.distRepoUrl,
				ecmTargetSystemInd:ex.ecmTargetSystemInd,ibdsTargetSystemInd:ex.ibdsTargetSystemInd,
				dataAccessStrategie:ex.dataAccessStrategie,it21DbDaoLocations:ex.it21DbDaoLocations,
				ibdsDbDaoLocations:ex.ibdsDbDaoLocations,serverContextPath:ex.serverContextPath,springProfiles:ex.springProfiles,
				webuiEmbedded:ex.webuiEmbedded)
			   filter(FixCrLfFilter.class,eol:FixCrLfFilter.CrLf.newInstance("lf"))
			  filteringCharset = 'UTF-8'
		}
	}
}
