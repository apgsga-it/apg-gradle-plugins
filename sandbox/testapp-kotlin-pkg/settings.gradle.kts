pluginManagement {
   resolutionStrategy {
      eachPlugin {
       	if (requested.id.id == "com.apgsga.rpm.package") {
          	useModule("com.apgsga.gradle:rpm-service-packager:1.0-SNAPSHOT")
      	} else if (requested.id.id == "com.apgsga.zip.package") {
          	useModule("com.apgsga.gradle:zip-service-packager:1.0-SNAPSHOT")
      	} else if (requested.id.id == "com.apgsga.publish") {
          	useModule("com.apgsga.gradle:generic-publish:1.0-SNAPSHOT")
      	} else if (requested.id.id == "com.apgsga.common.repo") {
          	useModule("com.apgsga.gradle:repo-config:1.0-SNAPSHOT")
      	} else if (requested.id.id == "com.apgsga.rpm.ssh.deployer") {
			useModule("com.apgsga.gradle:rpm-ssh-deployer:1.0-SNAPSHOT")
		}
      }
    }
    repositories {
		mavenLocal()
		gradlePluginPortal()
    }
}
rootProject.name = "testapp-kotlin-pkg"

