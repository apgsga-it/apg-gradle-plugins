package com.apgsga.gradle.repo.extensions;

import java.util.Map;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import com.google.common.collect.Maps;

public class LocalRepo extends AbstractRepo {

	public LocalRepo(Project project) {
		super(project);
	}

	@Override
	public String getDefaultRepoBaseUrl() {
		return project.getBuildDir().getAbsolutePath();
	}

	@Override
	public void log() {
		Logger logger = project.getLogger(); 
		logger.info("Local Repository Configuration is:" );
		logger.info(super.toString());
	}
}
