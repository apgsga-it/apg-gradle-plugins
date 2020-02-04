package com.apgsga.revision.manager.tasks

import com.apgsga.revision.manager.domain.RevisionManager
import com.apgsga.revision.manager.plugin.RevisionManagerPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.springframework.util.Assert

import javax.inject.Inject

class SaveRevision extends DefaultTask {

    private RevisionManager revisionManager

    @Inject
    SaveRevision(def revisionManager) {
        this.revisionManager = revisionManager
    }

    @TaskAction
    def doAction() {
        def target = project.getProperties().get("target")
        def revision = project.getProperties().get("revision")
        def fullRevisionPrefix = project.getProperties().get("fullRevisionPrefix")
        Assert.notNull(target, "target parameter missing when calling ${RevisionManagerPlugin.SAVE_REVISION_TASK_NAME} task")
        Assert.notNull(revision, "revision parameter missing when calling ${RevisionManagerPlugin.SAVE_REVISION_TASK_NAME} task")
        Assert.notNull(fullRevisionPrefix, "fullRevisionPrefix parameter missing when calling ${RevisionManagerPlugin.SAVE_REVISION_TASK_NAME} task")
        revisionManager.saveRevision(target,revision,fullRevisionPrefix)
    }
}