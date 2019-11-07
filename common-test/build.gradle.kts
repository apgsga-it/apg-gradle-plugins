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
    compileOnly( "org.codehaus.groovy:groovy-all:2.4.17")
}