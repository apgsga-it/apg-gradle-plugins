package com.apgsga.gradle.rpm.pkg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.redline_rpm.header.Architecture
import org.redline_rpm.header.Os
import org.redline_rpm.header.RpmType
import org.redline_rpm.payload.Directive

import groovy.io.FileType
// TODO (che, 2.10 ) Verfiy Ospackage Extension configuration as Task. Better ways?
class OsPackageConfigureTask extends DefaultTask {

	@TaskAction
	def taskAction() {
		final ExtensionContainer ext = project.getExtensions();
		def apgRpmPackage = project.extensions.apgRpmPackage
		def osPackageExtention = ext.getByName("ospackage")
		osPackageExtention.packageName = "${apgRpmPackage.targetServiceName}"
		osPackageExtention.version = "${apgRpmPackage.version}"
		osPackageExtention.release = "${apgRpmPackage.releaseNr}"
		osPackageExtention.os = Os.LINUX
		osPackageExtention.type = RpmType.BINARY
		osPackageExtention.arch = Architecture.NOARCH

		osPackageExtention.preInstall new File("$project.buildDir/rpm/pre-install.sh")
		osPackageExtention.postInstall new File("$project.buildDir/rpm/post-install.sh")
		osPackageExtention.preUninstall new File("$project.buildDir/rpm/pre-uninstall.sh")
		osPackageExtention.postUninstall new File("$project.buildDir/rpm/post-uninstall.sh")
		osPackageExtention.into "${apgRpmPackage.targetServiceExecDir}"
		osPackageExtention.user "${apgRpmPackage.targetServiceName}"
		osPackageExtention.permissionGroup "${apgRpmPackage.targetServiceName}"
		def copySpec = {
			// Strip the version from the jar filename
			rename { String fileName ->
				fileName.replace("-${project.version}", "")
			}
			fileMode 0644
			into "lib"
		}
		osPackageExtention.from("$project.buildDir/app-pkg/app/lib", copySpec)
		copySpec =  {
			include 'runJvm.sh'
			fileMode 0775
			into "bin"
		}
		osPackageExtention.from("$project.buildDir/app-pkg/app/bin", copySpec)
		copySpec = {
			include 'environment.sh'
			into "bin"
			fileMode 0775
			user 'root'
			permissionGroup "${apgRpmPackage.opsUserGroup}"
			addParentDirs false
			fileType Directive.CONFIG | Directive.NOREPLACE
		}
		osPackageExtention.from("$project.buildDir/app-pkg/app/bin",copySpec)
		copySpec = {
			into '/etc/systemd/system'
			include '*.*'
			addParentDirs false
			user 'root'
			permissionGroup 'root'
			fileMode = 0644
		}
		osPackageExtention.from("$project.buildDir/app-pkg/app/service",copySpec)

		// OPS Configuration files
		copySpec =  {
			into "${apgRpmPackage.targetServiceConfigDir}/ops"
			fileType Directive.CONFIG | Directive.NOREPLACE
			user "${apgRpmPackage.targetServiceName}"
			permissionGroup "${apgRpmPackage.opsUserGroup}"
			fileMode 0770
			addParentDirs false
		}

		osPackageExtention.from("$project.buildDir/app-pkg/app/conf/ops",copySpec)
		// APP Configuration files
		copySpec = {
			into "${apgRpmPackage.targetServiceConfigDir}/app"
			user 'root'
			permissionGroup 'root'
			fileMode 0775
		}
		osPackageExtention.from("$project.buildDir/app-pkg/app/conf/app",copySpec)
		osPackageExtention.directory("${apgRpmPackage.targetServiceDataDir}", 0775 )
		osPackageExtention.directory("${apgRpmPackage.targetServiceDataDir}/log", 0775 )
		
		
	}

}
