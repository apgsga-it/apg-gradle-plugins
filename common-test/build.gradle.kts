plugins {
    groovy
    `maven-publish`
    maven
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    compile("org.spockframework:spock-core:1.1-groovy-2.4") {
        exclude("","groovy-all")
    }
    compile("org.springframework:spring-core:5.2.1.RELEASE")
    compile(gradleTestKit())
}