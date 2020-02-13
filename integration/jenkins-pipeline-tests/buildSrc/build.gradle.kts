plugins {
    groovy
    `kotlin-dsl`
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


//tasks.compileGroovy {
//    dependsOn.remove(tasks.compileJava)
//}
//tasks.compileKotlin {
//    dependsOn(tasks.compileGroovy)
//}