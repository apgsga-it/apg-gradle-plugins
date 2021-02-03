package com.apgsga.maven.dm.plugin

import com.apgsga.maven.dm.ext.VersionResolutionExtension
import com.apgsga.revision.manager.domain.RevisionManagerBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class MergeRevision : DefaultTask() {

    @TaskAction
    fun merge() {
        val ext = project.extensions.findByName("apgVersionResolver") as VersionResolutionExtension
        val lastRev = ext.revisionManger.lastRevision(ext.serviceName,ext.installTarget)
        project.logger.info("Revision " + lastRev + " will be merged into main Revisions.json for service " + ext.serviceName + " and target " + ext.installTarget)
        val rm = RevisionManagerBuilder.create().revisionRootPath(project.gradle.gradleUserHomeDir.absolutePath).build();
        rm.saveRevision(ext.serviceName,ext.installTarget,lastRev,ext.bomBaseVersion)
    }
}