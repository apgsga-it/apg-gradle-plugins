apply {
    from ("${projectDir}/../gradle-common/common-plugin.gradle")
    from ("${projectDir}/../gradle-common/integration-test.gradle")
    from ("${projectDir}/../gradle-common/functional-test.gradle")
}

plugins {
    `kotlin-dsl`
}

dependencies {
    testCompile(gradleTestKit())
    testCompile(project(":common-test"))  {
        exclude("","groovy-all")
    }
    implementation ("org.slf4j", "slf4j-api", "1.7.21")

    implementation ("ch.qos.logback", "logback-classic" ,"1.1.11")
    implementation ("org.eclipse.aether", "aether-api", "1.1.0")
    implementation ("org.eclipse.aether","aether-spi", "1.1.0")
    implementation ("org.eclipse.aether:aether-util:1.1.0")
    implementation ("org.eclipse.aether:aether-impl:1.1.0")
    implementation ("org.eclipse.aether:aether-connector-basic:1.1.0")
    implementation ("org.eclipse.aether:aether-transport-file:1.1.0")
    implementation ("org.eclipse.aether:aether-transport-http:1.1.0")
    implementation ("org.apache.maven:maven-aether-provider:3.1.0")
    implementation ("org.eclipse.sisu:org.eclipse.sisu.plexus:0.1.1") {
        exclude ("javax.enterprise",  "cdi-api")
    }
    implementation (project(":common-repo"))
    implementation (project(":generic-publish"))

}
