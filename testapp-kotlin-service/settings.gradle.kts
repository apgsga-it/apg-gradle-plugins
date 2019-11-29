pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.apgsga.maven.publish") {
                useModule("com.apgsga.gradle:maven-publish:0.3-SNAPSHOT")
            } else if (requested.id.id == "com.apgsga.common.repo") {
                useModule("com.apgsga.gradle:common-repo:0.3-SNAPSHOT")
            } else if (requested.id.id == "com.apgsga.gradle.repo.config") {
                useModule("com.apgsga.gradle:repo-config:0.3-SNAPSHOT")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        mavenLocal()

    }
}
rootProject.name = "testapp-service-kotlin"