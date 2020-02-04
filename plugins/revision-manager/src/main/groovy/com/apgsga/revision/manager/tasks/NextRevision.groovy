package com.apgsga.revision.manager.tasks

import com.apgsga.revision.manager.domain.RevisionManager
import com.apgsga.revision.manager.plugin.RevisionManagerPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.springframework.util.Assert

import javax.inject.Inject

class NextRevision extends DefaultTask {

    private RevisionManager revisionManager

    public String lastRevForTarget = ""

    public String nextRevForTarget = ""

    @Inject
    NextRevision(RevisionManager revisionManager) {
        this.revisionManager = revisionManager
    }

    @TaskAction
    def doAction() {
        def target = project.getProperties().get("target")
        Assert.notNull(target, "target parameter missing when calling ${RevisionManagerPlugin.NEXT_REVISION_TASK_NAME} task")
        lastRevForTarget = revisionManager.lastRevision(target)
        nextRevForTarget = revisionManager.nextRevision()
    }
}
