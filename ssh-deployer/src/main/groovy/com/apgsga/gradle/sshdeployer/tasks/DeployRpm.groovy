package com.apgsga.gradle.sshdeployer.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class DeployRpm extends DefaultTask {

    @TaskAction
    def taskAction() {

        println "Task action from DeployRpm task !!!!!!!!!!!!!!!!!!!!!"

    }
}
