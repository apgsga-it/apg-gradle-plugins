package com.apgsga.gradle.repo.extensions;

import java.util.Map;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import com.google.common.collect.Maps;

public class RemoteRepo extends AbstractRepo {

	private static final String RELEASE_REPO_NAME_DEFAULT = "release-functionaltest";
	private static final String SNAPSHOT_REPO_NAME_DEFAULT = "snapshot-functionaltest";
	private static final String REPO_PW_DEFAULT = "gradledev-tests-user";
	private static final String REPO_USER_DEFAULT = "gradledev-tests-user";
	private static final String REPO_DEDFAULT = "rpm-functionaltest";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga"; 
	
	public RemoteRepo(Project project) {
		super(project);
	}
	
	public Map<String,String> getAvailableRepoNames() {
		Map<String, String> m = Maps.newHashMap();
		// TODO JHE: To be discussed, which repos we want in the list, and from where do we retrieve this list ... hardcoded here?
		m.put("rpm-publish", "yumpatchrepo");
		m.put("db-publish", "dbpatch");
		m.put("maven-publish", "releases");
		return m;
	}

	@Override
	public String getDefaultRepoBaseUrl() {
		return REPO_BASE_URL_DEFAULT;
	}

	@Override
	public String getDefaultRepoName() {
		return REPO_DEDFAULT;
	}


	@Override
	public String getDefaultUser() {
		return REPO_USER_DEFAULT; 
	}

	@Override
	public String getDefaultPassword() {
		return REPO_PW_DEFAULT; 
	}

	@Override
	public String getDefaultReleaseRepoName() {
		return RELEASE_REPO_NAME_DEFAULT;
	}

	@Override
	public String getDefaultSnapshotRepoName() {
		return SNAPSHOT_REPO_NAME_DEFAULT;
	}

	@Override
	public void log() {
		Logger logger = project.getLogger(); 
		logger.info("Remote Repository Configuration is:" );
		logger.info(super.toString());
	}

	


	

}
