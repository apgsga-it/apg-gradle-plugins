package com.apgsga.packaging.rpm.plugins


import com.netflix.gradle.plugins.rpm.Rpm
import org.gradle.api.Plugin
import org.gradle.api.Project

class OsPackagingPlugin implements Plugin<Project> {


   Project project
    Rpm rpmTask

    void apply(Project project) {

        this.project = project

        project.plugins.apply(OsPackagingBasePlugin.class)
        rpmTask = project.task([type: Rpm], 'buildRpm') as Rpm
    }
}
