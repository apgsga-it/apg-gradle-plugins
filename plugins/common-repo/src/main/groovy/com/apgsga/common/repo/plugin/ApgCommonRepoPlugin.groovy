package com.apgsga.common.repo.plugin

import com.apgsga.common.repo.extensions.LoaderManager
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

@CompileStatic
class ApgCommonRepoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.afterEvaluate {
            new LoaderManager(project).load();
        }
    }
}