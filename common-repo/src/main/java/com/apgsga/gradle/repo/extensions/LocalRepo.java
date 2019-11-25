package com.apgsga.gradle.repo.extensions;

import java.util.Map;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import com.google.common.collect.Maps;

public class LocalRepo extends AbstractRepo {

	private static final String TARGET_DIR_DEFAULT = "maventestrepo";
	private static final Map<RepoNames,String> repoNames;
	
	// JHE: Ok, overkill for local Repo, but we follow same logic as for Remote. And who knows, maybe we'll once have more than one local Repo?
	static {
		repoNames = Maps.newHashMap();
		repoNames.put(RepoNames.LOCAL, TARGET_DIR_DEFAULT);
	}
	
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

	@Override
	public Map<RepoNames, String> getDefaultRepoNames() {
		return repoNames;
	}
}
