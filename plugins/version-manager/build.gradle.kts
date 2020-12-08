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
    api(platform(project(":platform")))
    api(gradleTestKit())
    testImplementation(project(":common-test")) {
        exclude("", "groovy-all")
    }
    testImplementation("org.spockframework:spock-core") {
        exclude("","groovy-all")
    }
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.apgsga.patchframework:apg-patch-service-api") {
        exclude("","groovy-all")
    }
    kotlin("stdlib")
    implementation(kotlin("reflect"))
    implementation("org.apache.maven", "maven-model")
    implementation(project(":common-repo"))
    implementation(project(":revision-manager"))
    implementation("com.fasterxml.jackson.core", "jackson-databind")
    implementation("org.slf4j:slf4j-api")
    integrationTestRuntimeOnly("org.slf4j:slf4j-simple:1.7.29")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
