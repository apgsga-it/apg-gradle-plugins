import nu.studer.gradle.credentials.domain.CredentialsContainer
import com.google.common.collect.Lists

plugins {
	id("com.apgsga.rpm.package")
	id("com.apgsga.zip.package")
	id("com.apgsga.publish")
	id("org.hidetake.ssh") version "2.10.1"
	id("nu.studer.credentials")  version "1.0.7"
}
// Credentials

val credentials: CredentialsContainer by project.extra
val defaultUsername = credentials.getProperty("deployUser") as String
val defaultPassword = credentials.getProperty("deployUserPassword") as String

// Command Line Parameter and Defaults
// TODO (che, 6.11 ): Factor out Commandline Handling in Plugin
val parServiceName = "echoservice"
val parTargetHost =  if (project.hasProperty("targetHost")) project.property("targetHost").toString() else "jadas-e.apgsga.ch"
val parSshUser =  if (project.hasProperty("sshUser")) project.property("sshUser").toString() else defaultUsername
val parSshPw =  if (project.hasProperty("sshPw")) project.property("sshPw").toString() else defaultPassword
val parInstallTarget = if (project.hasProperty("installTarget")) project.property("installTarget").toString() else "CHEI212"
val parServiceVersion = if (project.hasProperty("serviceVersion")) project.property("serviceVersion").toString() else "1.0"
val parReleaseNr = if (project.hasProperty("releaseNr")) project.property("releaseNr").toString() else "1"
val parDownloadDir = if (project.hasProperty("downloadDir")) project.property("downloadDir").toString() else "downloads"


println ("Build script using the following parameters:")
println ("serviceName = $parServiceName")
println ("targetHost = $parTargetHost")
println ("installTarget = $parInstallTarget")
println ("serviceVersion = $parServiceVersion")
println ("releaseNr = $parReleaseNr")
println ("downloadDir = $parDownloadDir")



apgRepository {
	mavenLocal()
	mavenCentral()
}



apgPackage {
	serviceName = parServiceName
	// TODO (che,15.10) jadas-e services, neecs to be discussed how this list is maintained
	supportedServices = Lists.newArrayList("jadas", "digiflex","vkjadas", "interjadas", "interweb", parServiceName)
	dependencies = arrayOf("com.apgsga:testapp-service:0.1-SNAPSHOT")
	resourceFilters = "serviceport"
	appConfigFilters = "general"
  	servicePropertiesDir = "resources"
	installTarget = parInstallTarget
	mainProgramName  = "com.apgsga.testapp.TestappApplication"
	version = parServiceVersion 
	releaseNr = parReleaseNr
}

apgPackage.log()

apgGenericPublishConfig {
	artefactFile =  file ("${buildDir}/distributions/${apgPackage.archiveName}")
	local()
}

apgGenericPublishConfig.log()
