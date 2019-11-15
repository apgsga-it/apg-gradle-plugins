package com.apgsga.gradle.repo.extensions;

import java.util.Map;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import com.google.common.collect.Maps;


public class RemoteRepo extends AbstractRepo {

	// TODO JHE: Where to read the default from, json file? Gradle properties? Probably a JSON file, using Spring resource resolver/loader
	//	         The Map would be provided within a JSON file
	// TODO JHE: eventually provide a first simple stage plugin which provides the default value? Probably not

	private static final String RELEASE_REPO_NAME_DEFAULT = "release-functionaltest";
	private static final String SNAPSHOT_REPO_NAME_DEFAULT = "snapshot-functionaltest";
	private static final String RPM_REPO_NAME_DEFAULT = "rpm-functionaltest";
	private static final String REPO_USER_DEFAULT = "gradledev-tests-user";
	private static final String REPO_PW_DEFAULT = "gradledev-tests-user";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga";
	private static Map<String,String> repoNames;
	
	static {
		repoNames = Maps.newHashMap();
		repoNames.put(RepoNames.MAVEN_RELEASE.getName(), RELEASE_REPO_NAME_DEFAULT);
		repoNames.put(RepoNames.MAVEN_SNAPSHOT.getName(), SNAPSHOT_REPO_NAME_DEFAULT);
		repoNames.put(RepoNames.RPM.getName(), RPM_REPO_NAME_DEFAULT);
		repoNames.put(RepoNames.ZIP.getName(), RELEASE_REPO_NAME_DEFAULT);
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
	public String getDefaultPassword() {
		return REPO_PW_DEFAULT;
	}

	@Override
	public void log() {
		Logger logger = project.getLogger(); 
		logger.info("Remote Repository Configuration is:" );
		logger.info(super.toString());
	}

	@Override
	public Map<String, String> getDefaultRepoNames() {
		return repoNames;
	}

}
