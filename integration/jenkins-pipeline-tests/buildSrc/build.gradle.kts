plugins {
    groovy
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()

}


dependencies {
    testImplementation(gradleTestKit())
    implementation(kotlin("gradle-plugin"))
    implementation ("com.bmuschko:gradle-docker-plugin:6.1.3")
}
