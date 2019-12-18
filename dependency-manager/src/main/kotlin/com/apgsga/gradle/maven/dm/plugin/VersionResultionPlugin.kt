@file:Suppress("UnstableApiUsage")

package com.apgsga.gradle.maven.dm.plugin

import com.apgsga.gradle.maven.dm.ext.PatchVersionResolution
import com.apgsga.gradle.maven.dm.ext.PomVersionResolution
import com.apgsga.gradle.maven.dm.ext.VersionResolutionExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


open class VersionResultionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val boms = target.container(PomVersionResolution::class.java)
        val patches  = target.container(PatchVersionResolution::class.java)

        target.extensions.create("versionResolver", VersionResolutionExtension::class.java, target, boms, patches)

    }
}