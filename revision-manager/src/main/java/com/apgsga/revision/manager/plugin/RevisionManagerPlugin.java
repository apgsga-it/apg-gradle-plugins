package com.apgsga.revision.manager.plugin;

import com.apgsga.revision.manager.tasks.AddRevision;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

@NonNullApi
public class RevisionManagerPlugin implements Plugin<Project> {

    public static final String PLUGIN_ID = "com.apgsga.revision.manager";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        project.getTasks().register("addRevision", AddRevision.class);
    }
}
