package com.apgsga.revision.manager.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class AddRevision extends DefaultTask {

    @TaskAction
    def doAction() {
        println "This is the addRevision task..."
    }
}
