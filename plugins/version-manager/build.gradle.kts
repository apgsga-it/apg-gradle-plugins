apply {
    from ("${projectDir}/../gradle-common/common-plugin.gradle")
    from ("${projectDir}/../gradle-common/integration-test.gradle")
    from ("${projectDir}/../gradle-common/functional-test.gradle")
}

plugins {
    `kotlin-dsl`
}

val integrationTestRuntimeOnly: Configuration by configurations.getting {
    extendsFrom(configurations.testRuntimeOnly.get())
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    api(gradleTestKit())
    testImplementation(project(":common-test")) {
        exclude("", "groovy-all")
    }
    testImplementation("org.spockframework:spock-core:1.2-groovy-2.5") {
        exclude("","groovy-all")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    implementation("com.apgsga.patchframework:apg-patch-service-api:2.0.0-SNAPSHOT") {
        exclude("","groovy-all")
    }
    kotlin("stdlib")
    implementation(kotlin("reflect"))
    implementation("org.apache.maven", "maven-model", "3.0.2")
    implementation(project(":common-repo"))
    implementation(project(":revision-manager"))
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.10.0")
    implementation("org.slf4j:slf4j-api:1.7.25")
    integrationTestRuntimeOnly("org.slf4j:slf4j-simple:1.7.29")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
