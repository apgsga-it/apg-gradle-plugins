//package com.apgsga.gradle.maven.tasks
//
//import org.apache.maven.cli.MavenCli
//import org.gradle.api.DefaultTask
//import org.gradle.api.tasks.TaskAction
//
//class InitMaven extends DefaultTask {
//
//    @TaskAction
//    def init() {
//        // JHE: why this -> https://stackoverflow.com/questions/33400574/unable-to-run-maven-tasks-through-mavencli-maven-embedder
//        System.setProperty("maven.multiModuleProjectDirectory","")
//
//        MavenCli mc = new MavenCli()
//        mc.doMain(["clean","install","-Dmaven.repo.local=${project.rootProject.projectDir}${File.separator}mavenTestLocalRepo","-X"].toArray() as String[], "${project.rootProject.projectDir}${File.separator}initProjects${File.separator}mavenInit", System.out, System.out)
//    }
//}
