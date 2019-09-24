package com.apgsga.gradle.repo.extensions;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

public class LocalRepo extends AbstractRepo {

	private static final String TARGET_DIR_DEFAULT = "maventestrepo";
	private static final String LOCAL_REPO_BASE_DIR_DEFAULT = "build";

	public LocalRepo(Project project) {
		super(project);
	}

	@Override
	public String getDefaultRepoBaseUrl() {
		return LOCAL_REPO_BASE_DIR_DEFAULT;
	}

	@Override
	public String getDefaultRepoName() {
		return TARGET_DIR_DEFAULT;
	}
	
	
	@Override
	public String getDefaultReleaseRepoName() {
		return TARGET_DIR_DEFAULT;
	}

	@Override
	public String getDefaultSnapshotRepoName() {
		return TARGET_DIR_DEFAULT;
	}

	@Override
	public void log() {
		Logger logger = project.getLogger(); 
		logger.info("Local Repository Configuration is:" );
		logger.info(super.toString());
	}


}
