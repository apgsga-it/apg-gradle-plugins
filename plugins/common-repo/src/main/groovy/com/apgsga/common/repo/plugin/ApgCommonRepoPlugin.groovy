package com.apgsga.common.repo.plugin

import groovy.transform.CompileStatic
import com.apgsga.common.repo.extensions.LoaderManager
import com.apgsga.common.repo.extensions.MavenSettingsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

@CompileStatic
class ApgCommonRepoPlugin implements Plugin<Project> {

    public static final String MAVEN_SETTINGS_EXTENSION_NAME = "mavenSettings"

    @Override
    void apply(Project project) {
        project.extensions.create(MAVEN_SETTINGS_EXTENSION_NAME, MavenSettingsExtension.class, project)
        //new LoaderManager(project).load();
        //TODO JHE: Really needed and what we want?
        project.getPlugins().apply("maven-publish")

    }
}