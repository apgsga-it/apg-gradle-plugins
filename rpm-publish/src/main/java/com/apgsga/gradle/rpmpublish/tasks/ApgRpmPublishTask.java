package com.apgsga.gradle.rpmpublish.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

import com.apgsga.gradle.rpmpublish.ApgRpmPublishConfig;

public class ApgRpmPublishTask extends DefaultTask  {
	
	@TaskAction
    public void publish()  {
		Logger logger = getLogger(); 
		logger.info("Starting ApgRpmPublishTask");
		ApgRpmPublishConfig config = getProject().getExtensions().findByType(ApgRpmPublishConfig.class);
		config.logConfig();
		logger.info("ApgRpmPublishTask done.");
		
	}

}
