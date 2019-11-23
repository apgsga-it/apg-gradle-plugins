
apply {
    from ("${projectDir}/../gradle-common/common-plugin.gradle")
    from ("${projectDir}/../gradle-common/integration-test.gradle")
    from ("${projectDir}/../gradle-common/functional-test.gradle")
}

plugins {
    `kotlin-dsl`
    `project-report`
}

val integrationTestRuntimeOnly: Configuration by configurations.getting {
    extendsFrom(configurations.testRuntimeOnly.get())
}

dependencies {
    testCompile(gradleTestKit())
    testCompile(project(":common-test")) {
        exclude("", "groovy-all")
    }
    compile(kotlin("stdlib"))
    runtime(kotlin("reflect"))
    compile("org.apache.maven", "maven-model", "3.0.2")

    compile(project(":common-repo"))
    compile(project(":generic-publish"))
    implementation("org.slf4j:slf4j-api:1.7.25")
    integrationTestRuntimeOnly("org.slf4j:slf4j-simple:1.7.29")

}

