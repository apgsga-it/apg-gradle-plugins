import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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

tasks {
    val compileJava = named("compileJava", JavaCompile::class).get()
    val compileKotlin = named("compileKotlin", KotlinCompile::class).get()
    val compileGroovy = named("compileGroovy", GroovyCompile::class).get()
    val classes by getting

    compileGroovy.dependsOn.remove("compileJava")
    compileKotlin.setDependsOn(mutableListOf(compileGroovy))
    compileKotlin.classpath += files(compileGroovy.destinationDir)
    classes.setDependsOn(mutableListOf(compileKotlin))
}
