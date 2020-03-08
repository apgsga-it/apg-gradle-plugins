package com.apgsga.gradle.jenkinsrunner


import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import java.io.ByteArrayOutputStream


open class JenkinsRunnerExt(private val project: Project) {
    var jenkinsDirPath : String = "${project.projectDir.absolutePath}/jenkins"
    var jenkinsWorkspaceDirPath : String = "${project.projectDir.absolutePath}/../modules"
    fun runnerDefaultParameter() : List<String> {
        return listOf("-w" , "${jenkinsDirPath}/lib/jenkins.war", "-p" , "${jenkinsDirPath}/plugins", "--runWorkspace", jenkinsWorkspaceDirPath)
    }
    fun log() {
        project.logger.log(LogLevel.INFO, toString())
    }

    override fun toString(): String {
        return "JenkinsRunnerExt(jenkinsDirPath='$jenkinsDirPath', jenkinsWorkspaceDirPath='$jenkinsWorkspaceDirPath')"
    }

}

class JenkinsRunner : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        apply<JavaPlugin>()
        project.extensions.create<JenkinsRunnerExt>("jenkinsRunnerConfig", this)
    }
}

open class JenkinsRunnerExec : Exec() {

    @Input
    var testParameters : Collection<String> = listOf()
    @Input
    var jenkinsFilePath : String = ""


    override fun exec() {
        val parameters = project.extensions["jenkinsRunnerConfig"] as JenkinsRunnerExt
        val command = if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            "${parameters.jenkinsDirPath}/runner/bin/jenkinsfile-runner.bat"
        } else if (Os.isFamily(Os.FAMILY_MAC) || Os.isFamily(Os.FAMILY_UNIX)) {
            "${parameters.jenkinsDirPath}/runner/bin/jenkinsfile-runner"
        } else {
            throw IllegalArgumentException("Unkown OS Family")
        }
        val cmd = mutableListOf<String>()
        cmd.add(command)
        cmd.addAll(parameters.runnerDefaultParameter())
        cmd.add("-f")
        cmd.add(jenkinsFilePath)
        cmd.addAll(testParameters)
        commandLine = cmd
        workingDir(project.projectDir)
        logger.info("Command : ${commandLine.toString()}")
        super.exec()
    }

    override fun doLast(action: Action<*>): Task {
        return super.doLast(action)
    }
}