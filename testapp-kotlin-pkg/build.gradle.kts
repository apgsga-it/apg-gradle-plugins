import nu.studer.gradle.credentials.domain.CredentialsContainer
import com.google.common.collect.Lists

plugins {
  id("com.apgsga.rpm.package") 
  id("com.apgsga.zip.package") 
  id("com.apgsga.publish") 
  id("org.hidetake.ssh") version "2.10.1"
  id("nu.studer.credentials")  version "1.0.7"
}
// Credetilas

val credentials: CredentialsContainer by project.extra
val defaultUsername = credentials.getProperty("deployUser") as String
val defaultPassword = credentials.getProperty("deployUserPassword") as String

// Command Line Parameter and Defaults
// TODO (che, 6.11 ): Factor out Commandline Handling in Plugin
val parServiceName = "echoservice"
val parTargetHost =  (fun(): String? { return (if (project.hasProperty("targetHost")) project.property("targetHost") else "jadas-e.apgsga.ch") as String? }).invoke()
val parSshUser = (fun(): String? { return (if (project.hasProperty("sshUser")) project.property("sshUser") else defaultUsername) as String? }).invoke()
val parSshPw = (fun(): String? { return (if (project.hasProperty("sshPw")) project.property("sshPw") else defaultPassword) as String? }).invoke()
val parInstallTarget = (fun(): String? { return (if (project.hasProperty("installTarget")) project.property("installTarget") else "CHEI212") as String? }).invoke()
val parServiceVersion = (fun(): String? { return (if (project.hasProperty("serviceVersion")) project.property("serviceVersion") else "1.0") as String? }).invoke()
val parReleaseNr = (fun(): String? { return (if (project.hasProperty("releaseNr")) project.property("releaseNr") else "1") as String? }).invoke()
val parDownloadDir = (fun(): String? { return (if (project.hasProperty("downloadDir")) project.property("downloadDir") else "downloads") as String? }).invoke()


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
