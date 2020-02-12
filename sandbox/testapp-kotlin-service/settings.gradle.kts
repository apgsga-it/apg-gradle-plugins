pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.apgsga.maven.publish") {
                useModule("com.apgsga.gradle:maven-publish:1.0-SNAPSHOT")
            } else if (requested.id.id == "com.apgsga.common.repo") {
                useModule("com.apgsga.gradle:common-repo:1.0-SNAPSHOT")
            } else if (requested.id.id == "com.apgsga.gradle.repo.config") {
                useModule("com.apgsga.gradle:repo-config:1.0-SNAPSHOT")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        mavenLocal()

    }
}
rootProject.name = "testapp-service-kotlin"