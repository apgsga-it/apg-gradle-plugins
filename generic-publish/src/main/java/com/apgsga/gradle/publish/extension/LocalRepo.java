package com.apgsga.gradle.publish.extension;

import java.io.File;

public class LocalRepo extends AbstractRepo  {

	private static final String TARGET_DIR_DEFAULT = "maventestrepo";
	private static final String LOCAL_REPO_BASE_DIR_DEFAULT = "build";

	public LocalRepo() {
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
	public Boolean getDefaultPublish() {
		return false; 
	}


}
