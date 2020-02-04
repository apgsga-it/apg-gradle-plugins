package com.apgsga.revision.manager.plugin;

import com.apgsga.revision.manager.domain.RevisionManagerImpl;
import com.apgsga.revision.manager.tasks.NextRevision;
import com.apgsga.revision.manager.tasks.SaveRevision;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Properties;

@NonNullApi
public class RevisionManagerPlugin implements Plugin<Project> {

    public static final String PLUGIN_ID = "com.apgsga.revision.manager";
    public static final String SAVE_REVISION_TASK_NAME = "saveRevision";
    public static final String NEXT_REVISION_TASK_NAME = "nextRevision";
    public static final String REVISION_FILE_PATH_PROPERTY = "revision.file.path";
    public static final String CONFIG_DIR_PROPERTY = "config.dir";

    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        RevisionManagerImpl revisionManager = new RevisionManagerImpl(loadConfigurationProperties());
        project.getTasks().register(SAVE_REVISION_TASK_NAME, SaveRevision.class, revisionManager);
        project.getTasks().register(NEXT_REVISION_TASK_NAME, NextRevision.class, revisionManager);
    }

    private Properties loadConfigurationProperties() {

        // TODO JHE: Properties might be loaded differently with IT-35189

        Properties props = new Properties();
        props.put(REVISION_FILE_PATH_PROPERTY, project.getProperties().get(REVISION_FILE_PATH_PROPERTY));
        props.put(CONFIG_DIR_PROPERTY, project.getProperties().get(CONFIG_DIR_PROPERTY));
        return props;
    }
}
