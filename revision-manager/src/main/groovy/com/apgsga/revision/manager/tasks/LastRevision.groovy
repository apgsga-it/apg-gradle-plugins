package com.apgsga.revision.manager.tasks

import com.apgsga.revision.manager.domain.RevisionManager
import com.apgsga.revision.manager.plugin.RevisionManagerPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.TaskAction
import org.springframework.util.Assert

import javax.inject.Inject

class LastRevision extends DefaultTask {

    private RevisionManager revisionManager

    public String lastRev = "";

    @Inject
    LastRevision(RevisionManager revisionManager) {
        this.revisionManager = revisionManager
    }

    @TaskAction
    def doAction() {
        def target = project.getProperties().get("target")
        Assert.notNull(target, "target parameter missing when calling ${RevisionManagerPlugin.LAST_REVISION_TASK_NAME} task")
        // TODO JHE: Obvious but ... don't forget to change this :)
//        lastRev = revisionManager.lastRevision(target)
        lastRev = "this_is_a_hardcoded_version"
    }
}
