package com.apgsga.common.repo.extensions


import org.gradle.api.Project

class MavenSettingsExtension {
    private Project project

    /**
     * Name of settings file to use. String is evaluated using {@link org.gradle.api.Project#file(java.lang.Object)}.
     * Defaults to $USER_HOME/.m2/settings.xml.
     */
    String userSettingsFileName = System.getProperty("user.home") + "/.m2/settings.xml"

    void setUserSettingsFileName(String userSettingsFileName) {
        this.userSettingsFileName = userSettingsFileName;
        LoaderManager lm = new LoaderManager(project);
        lm.load();
    }

    /**
     * List of profile ids to treat as active.
     */
    String[] activeProfiles = []

    /**
     * Flag indicating whether or not Gradle project properties should be exported for the purposes of settings file
     * property interpolation and profile activation. Defaults to true.
     */
    boolean exportGradleProps = true

    MavenSettingsExtension(Project project) {
        this.project = project
    }

    File getUserSettingsFile() {
        return project.file(userSettingsFileName)
    }
}