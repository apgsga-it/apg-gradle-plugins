pluginManagement {
   resolutionStrategy {
      eachPlugin {
       	if (requested.id.id == "com.apgsga.rpm.package") {
          	useModule("com.apgsga.gradle:rpm-service-packager:0.3-SNAPSHOT")
      	} else if (requested.id.id == "com.apgsga.zip.package") {
          	useModule("com.apgsga.gradle:zip-service-packager:0.3-SNAPSHOT")
      	} else if (requested.id.id == "com.apgsga.publish") {
          	useModule("com.apgsga.gradle:generic-publish:0.3-SNAPSHOT")
      	} else if (requested.id.id == "com.apgsga.common.repo") {
          	useModule("com.apgsga.gradle:repo-config:0.3-SNAPSHOT")
      	}
      }
    }
    repositories {
    	mavenLocal()
		gradlePluginPortal()
    }
}
rootProject.name = "testapp-kotlin-pkg"
