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

dependencies {
    testImplementation(gradleTestKit())
    testCompile(project(":common-test")) {
        exclude("", "groovy-all")
    }
    testCompile("org.spockframework:spock-core:1.1-groovy-2.4") {
        exclude("","groovy-all")
    }
    testCompile("org.junit.jupiter:junit-jupiter:5.5.2")
    compile("com.apgsga.patchframework:apg-patch-service-common:1.3.14") {
        exclude("","groovy-all")
    }
    kotlin("stdlib")
    compile(kotlin("reflect"))
    compile("org.apache.maven", "maven-model", "3.0.2")
    compile(project(":common-repo"))
    compile(project(":generic-publish"))
    compile("org.slf4j:slf4j-api:1.7.25")
    integrationTestRuntimeOnly("org.slf4j:slf4j-simple:1.7.29")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
