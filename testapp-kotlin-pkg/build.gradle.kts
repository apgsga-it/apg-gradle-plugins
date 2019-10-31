import nu.studer.gradle.credentials.domain.CredentialsContainer
import com.google.common.collect.Lists

plugins {
  id("com.apgsga.rpm.package") 
  id("com.apgsga.zip.package") 
  id("com.apgsga.publish") 
  id("org.hidetake.ssh") version "2.10.1"
  id("nu.studer.credentials")  version "1.0.7"
}

val credentials: CredentialsContainer by project.extra
val defaultUsername = credentials.getProperty("deployUser") as String
val defaultPassword = credentials.getProperty("deployUserPassword") as String
val parServiceName: String by project.extra("echoservice")
val parTargetHost: String by project.extra("jadas-e.apgsga.ch")
val parSshUser: String by project.extra(defaultUsername)
val parSshPw: String by project.extra(defaultPassword)
val parInstallTarget: String by project.extra("CHEI212")
val parServiceVersion: String by project.extra("1.0")
val parReleaseNr: String by project.extra("1")
val parDownloadDir: String by project.extra("downloads")


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

