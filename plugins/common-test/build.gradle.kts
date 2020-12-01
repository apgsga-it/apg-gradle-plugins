plugins {
    groovy
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.spockframework:spock-core:1.2-groovy-2.5") {
        exclude("","groovy-all")
    }
    implementation("org.springframework:spring-core:5.2.1.RELEASE")
    implementation(gradleTestKit())
}