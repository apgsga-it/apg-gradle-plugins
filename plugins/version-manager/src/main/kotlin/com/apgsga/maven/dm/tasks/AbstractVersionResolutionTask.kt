package com.apgsga.maven.dm.tasks

import com.apgsga.maven.VersionResolver
import com.apgsga.maven.dm.ext.VersionResolutionExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AbstractVersionResolutionTask : DefaultTask() {

    @TaskAction
    fun doAction() {
        val resolutionExtension = project.extensions.getByType(VersionResolutionExtension::class.java)
        doResolutionAction(resolutionExtension.versionResolver, resolutionExtension)
    }

    abstract fun doResolutionAction(versionResolver: VersionResolver, resolutionExtension: VersionResolutionExtension)


}


