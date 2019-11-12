package com.apgsga.gradle.repo.extensions;

import java.util.Map;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import com.google.common.collect.Maps;

public class RemoteRepo extends AbstractRepo {

	private static final String RELEASE_REPO_NAME_DEFAULT = "release-functionaltest";
	private static final String SNAPSHOT_REPO_NAME_DEFAULT = "snapshot-functionaltest";
	private static final String REPO_USER_DEFAULT = "gradledev-tests-user";
	private static final String REPO_DEDFAULT = "rpm-functionaltest";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga";
	private static Map<String,String> repoNames;
	
	static {
		repoNames = Maps.newHashMap();
		repoNames.put(RepoNames.GENERIC.toString(), REPO_DEDFAULT);
		repoNames.put(RepoNames.MAVEN.toString(), RELEASE_REPO_NAME_DEFAULT);
		repoNames.put(RepoNames.SNAPSHOT.toString(), SNAPSHOT_REPO_NAME_DEFAULT);
	}
	
	public RemoteRepo(Project project) {
		super(project);
	}
	
	@Override
	public String getDefaultRepoBaseUrl() {
		return REPO_BASE_URL_DEFAULT;
	}

	@Override
	public String getDefaultUser() {
		return REPO_USER_DEFAULT; 
	}

	@Override
	public void log() {
		Logger logger = project.getLogger(); 
		logger.info("Remote Repository Configuration is:" );
		logger.info(super.toString());
	}

	@Override
	public Map<String, String> getRepoNames() {
		return repoNames;
	}

}
