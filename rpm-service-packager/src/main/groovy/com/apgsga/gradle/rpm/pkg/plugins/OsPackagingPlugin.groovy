package com.apgsga.gradle.rpm.pkg.plugins

import com.netflix.gradle.plugins.packaging.ProjectPackagingExtension
import com.netflix.gradle.plugins.rpm.Rpm
import com.netflix.gradle.plugins.rpm.RpmPlugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.ConventionMapping
import org.gradle.api.internal.IConventionAware

class OsPackagingPlugin implements Plugin<Project> {


   Project project
    Rpm rpmTask

    void apply(Project project) {

        this.project = project

        project.plugins.apply(OsPackagingBasePlugin.class)
        rpmTask = project.task([type: Rpm], 'buildRpm') as Rpm
    }
}
