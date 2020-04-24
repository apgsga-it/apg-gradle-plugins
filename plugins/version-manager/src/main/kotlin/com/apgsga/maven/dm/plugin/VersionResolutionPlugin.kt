package com.apgsga.maven.dm.plugin

import com.apgsga.maven.dm.ext.VersionResolutionExtension
import com.apgsga.packaging.plugins.ApgCommonPackagePlugin
import com.apgsga.revision.manager.domain.RevisionManagerBuilder
import net.linguica.gradle.maven.settings.MavenSettingsPlugin
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin


open class VersionResolutionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(MavenSettingsPlugin::class.java)
        project.plugins.apply(MavenPublishPlugin::class.java)
        project.plugins.apply(ApgCommonPackagePlugin::class.java)
        val revisionManagerBuilder = RevisionManagerBuilder.create()
        val extension =  project.extensions.create("apgVersionResolver", VersionResolutionExtension::class.java, project, revisionManagerBuilder)
        applyRecommendations(project, extension)

    }
    private fun applyRecommendations(project: Project, resolutionExtension : VersionResolutionExtension) {
        val config = project.configurations.findByName(resolutionExtension.configurationName)
        val action = Action<Configuration> {
            if (this == config) {
                if (state == Configuration.State.UNRESOLVED) {
                    resolutionStrategy.eachDependency {
                        // TODO (che, jhe , 27.9 ) : Needs to be discussed, basically apg version resolution takes precedence over specified version
                        var versionUsed = resolutionExtension.versionResolver.getVersion(requested.group, requested.name)
                        if (versionUsed.isEmpty() && !requested.version.isNullOrEmpty() ) versionUsed = requested.version!!
                        this.useVersion(versionUsed)
                        this.because("Apg Version Resolver ")
                    }
                }
            }

        }
        project.configurations.all(action)
    }

}



