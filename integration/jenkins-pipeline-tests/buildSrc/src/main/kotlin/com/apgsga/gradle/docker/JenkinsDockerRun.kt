package com.apgsga.gradle.docker


import com.bmuschko.gradle.docker.DockerRemoteApiPlugin
import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerLogsContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStopContainer
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.network.DockerCreateNetwork
import com.bmuschko.gradle.docker.tasks.network.DockerRemoveNetwork
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import kotlin.collections.set
import org.apache.tools.ant.taskdefs.condition.Os

open class JenkinsDockerRunExt(private val project: Project) {
    // TODO (che, 12.2 ) migrate properties to configurable Properties, centralize more
    val jenkinsHome = "/jenkinsHome"
    val networkName = "jenkinstestnet"
    val defaultCmd = mutableListOf<String>()
    val defaultVolumes: MutableMap<String, String> = mutableMapOf()
    private val testModulesDir = "/../modules"
    val testCmd: ListProperty<String> = project.objects.listProperty(String::class.java)

    init {
        // Default Bind Volumes
        defaultVolumes[project.rootDir.absolutePath + "/jenkinsHome"] = jenkinsHome
        defaultVolumes[project.repositories.mavenLocal().url.path] = "/mavenLocal"
        defaultVolumes[project.rootDir.absolutePath + "/gradleTestUserHome"] = "/gradleUserHome"
        defaultVolumes[project.projectDir.absolutePath + "/Jenkinsfile"] = "/workspace/Jenkinsfile"
        defaultVolumes[project.rootDir.absolutePath + testModulesDir] = "/workspace"
        // Default Cmd
        defaultCmd.add("--runWorkspace")
        defaultCmd.add("/jenkinsWorkspace")
    }

    fun log() {
        project.logger.log(LogLevel.INFO, toString())
    }


}

class JenkinsDockerRun : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        apply<DockerRemoteApiPlugin>()
        apply<JavaPlugin>()
        val extension = project.extensions.create<JenkinsDockerRunExt>("runJenkinsDocker", this)
        val buildSshdImage = project.tasks.register<DockerBuildImage>("buildSshdImage") {
            inputDir.set(file("$rootDir/src/docker/sshd"))
            images.set(listOf("apg-sshd-image:latest"))
            noCache.set(false)
            pull.set(false)
        }
        val createInstallTarget = project.tasks.register<DockerCreateContainer>("createInstallTarget") {
            containerName.set("installTarget")
            targetImageId(buildSshdImage.get().imageId)
            hostConfig.portBindings.set(listOf("2202:22"))
            hostConfig.autoRemove.set(true)
            hostConfig.network.set(extension.networkName)
            hostConfig.privileged.set(true)
            dependsOn(buildSshdImage)
        }
        val startInstallTarget = project.tasks.register<DockerStartContainer>("startInstallTarget") {
            targetContainerId(createInstallTarget.get().containerId)
            dependsOn(createInstallTarget)
        }
        val buildJenkinsRunnerImage = project.tasks.register<DockerBuildImage>("jenkinsBuildImage") {
            inputDir.set(file("$rootDir/src/docker/jenkins"))
            images.set(listOf("apg-jenkinsrunner-image:latest"))
            noCache.set(false)
            pull.set(false)
        }

        val createJenkinsRunnerContainer = project.tasks.register<DockerCreateContainer>("createJenkinsRunnerContainer") {
            targetImageId(buildJenkinsRunnerImage.get().imageId)
            cmd.addAll(extension.defaultCmd)
            cmd.addAll(extension.testCmd)
            hostConfig.binds.set(patchVolumesForWindows(project, extension.defaultVolumes))
            hostConfig.autoRemove.set(true)
            hostConfig.network.set(extension.networkName)
            attachStdout.set(true)
            attachStderr.set(true)
            dependsOn(buildJenkinsRunnerImage)
        }
        val startJenkinsRunnerContainer = project.tasks.register<DockerStartContainer>("startJenkinsRunnerContainer") {
            targetContainerId(createJenkinsRunnerContainer.get().containerId)
            dependsOn(createJenkinsRunnerContainer)
        }
        val logJenkinsRunnerContainer = project.tasks.register<DockerLogsContainer>("logJenkinsRunnerContainer") {
            targetContainerId(createJenkinsRunnerContainer.get().containerId)
            follow.set(true)
            tailAll.set(true)
            onNext { logger.info(this.toString()) }
            dependsOn(startJenkinsRunnerContainer)
        }
        val stopInstallTarget = project.tasks.register<DockerStopContainer>("stopInstallTarget") {
            targetContainerId(createInstallTarget.get().containerId)
            dependsOn(startInstallTarget)
        }
        project.tasks.register<DockerCreateNetwork>("createNetwork") {
            networkId.set(extension.networkName)
        }
        project.tasks.register<DockerRemoveNetwork>("removeNetwork") {
            networkId.set(extension.networkName)
        }



        val cleanJenkinsHome = project.tasks.register<DefaultTask>("cleanJenkinsHome") {
            outputs.upToDateWhen { false }
            doLast {
                fileTree(("${rootDir}/${extension.jenkinsHome}")).visit {
                    logger.info("About to delete: " + this.file.name)
                    val deleted = delete(this.file)
                    logger.info("File deleted $deleted")
                }
            }
        }

        tasks.register<DefaultTask>("runAll") {
            buildJenkinsRunnerImage.get().dependsOn.add(buildSshdImage)
            startJenkinsRunnerContainer.get().dependsOn.add(startInstallTarget)
            stopInstallTarget.get().dependsOn.add(logJenkinsRunnerContainer)
            dependsOn(stopInstallTarget.get(), cleanJenkinsHome.get())
        }

        tasks.register<DefaultTask>("runJenkins") {
            dependsOn(buildJenkinsRunnerImage.get(), logJenkinsRunnerContainer.get(), cleanJenkinsHome.get())
        }
    }

    private fun patchVolumesForWindows(project: Project, defaultVolumes: MutableMap<String, String>): MutableMap<String, String> {
        defaultVolumes.forEach { (key, value) ->
            project.logger.info("Host binds, key: ${key} , value : ${value}")
        }
        if (!Os.isFamily(Os.FAMILY_WINDOWS)) return defaultVolumes
        Volumes.hello("Hi there from windows")
        return Volumes.convert(defaultVolumes)
    }





}

