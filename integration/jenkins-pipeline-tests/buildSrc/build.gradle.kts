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
    implementation ("org.hidetake:gradle-ssh-plugin:2.10.1")
    implementation ("com.apgsga.gradle:ssh-common:1.1-SNAPSHOT")
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
