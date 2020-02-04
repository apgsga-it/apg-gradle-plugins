package com.apgsga.gradle.rpm.pkg.tasks


import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class RpmScriptsCopyTask extends DefaultTask {

	@OutputDirectory
    File getOutputDir() {
		return new File("${project.buildDir}/rpm")
	}


	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgPackage
		project.copy() {
			
			from "${project.buildDir}/template/rpm"
			into "${project.buildDir}/rpm"
			include "**/*.sh"
			expand(serviceName:ex.serviceName,installTarget:ex.installTarget,mainPrg:ex.mainProgramName,targetServiceName:ex.targetServiceName,
				targetServiceExecDir:ex.targetServiceExecDir,targetServiceDataDir:ex.targetServiceDataDir,
				targetServiceConfigDir:ex.targetServiceConfigDir,javaDir:ex.javaDir,
				javaDist:ex.javaDist,distRepoUrl:ex.distRepoUrl)
			filteringCharset = 'UTF-8'
		}
	}
}
