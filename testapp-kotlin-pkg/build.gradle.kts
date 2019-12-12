import nu.studer.gradle.credentials.domain.CredentialsContainer
import com.google.common.collect.Lists

plugins {
    id("com.apgsga.rpm.package")
    id("com.apgsga.zip.package")
    id("com.apgsga.publish")
    id("org.hidetake.ssh") version "2.10.1"
    id("nu.studer.credentials") version "1.0.7"
}
// Credentials

val credentials: CredentialsContainer by project.extra
val defaultUsername = credentials.getProperty("deployUser") as String
val defaultPassword = credentials.getProperty("deployUserPassword") as String

// TODO (che, 6.11 ): Factor out Commandline Handling in Plugin

// Command Line Parameter Defaults

val parServiceName by extra("echoservice")
var parTargetHost by extra("jadas-e.apgsga.ch")
var parSshUser by extra(defaultUsername)
var parSshPw by extra(defaultPassword)
var parInstallTarget by extra("CHEI212")
var parServiceVersion by extra("1.0")
var parReleaseNr by extra("1")
var parDownloadDir by extra("downloads")

// Command Line Parameter Processing
println("Build script using the following parameters:")
println("serviceName = $parServiceName")
if (project.hasProperty("targetHost")) {
    parTargetHost = project.property("targetHost").toString()
}
println("targetHost = $parTargetHost")
if (project.hasProperty("installTarget")) {
    parInstallTarget = project.property("installTarget").toString()
}
println("installTarget = $parInstallTarget")
if (project.hasProperty("sshUser")) {
    parSshUser = project.property("sshUser").toString()
}
if (project.hasProperty("sshPw")) {
    parSshPw = project.property("sshPw").toString()
}
if (project.hasProperty("serviceVersion")) {
    parServiceVersion = project.property("serviceVersion").toString()
}
println("serviceVersion = $parServiceVersion")
if (project.hasProperty("releaseNr")) {
    parReleaseNr = project.property("releaseNr").toString()
}
println("releaseNr = $parReleaseNr")
if (project.hasProperty("downloadDir")) {
    parDownloadDir = project.property("downloadDir").toString()
}
println("downloadDir = $parDownloadDir")


apgRepositories {
    mavenLocal()
    mavenCentral()
}



apgPackage {
    serviceName = parServiceName
    // TODO (che,15.10) jadas-e services, neecs to be discussed how this list is maintained
    supportedServices = Lists.newArrayList("jadas", "digiflex", "vkjadas", "interjadas", "interweb", parServiceName)
    dependencies = arrayOf("com.apgsga:testapp-service-kotlin:0.1-SNAPSHOT")
    resourceFilters = "serviceport"
    appConfigFilters = "general"
    servicePropertiesDir = "resources"
    installTarget = parInstallTarget
    mainProgramName = "com.apgsga.testapp.TestappApplicationKt"
    version = parServiceVersion
    releaseNr = parReleaseNr

}

apgPackage.log()

apgGenericPublishConfig {
    artefactFile = file("${buildDir}/distributions/${apgPackage.archiveName}")
    local()
}

apgGenericPublishConfig.log()
