package com.apgsga.gradle.mavenpublish;

import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.logging.Logger;
import org.gradle.api.publish.PublishingExtension;

public class RemoteRepo {

	private static final String REPO_PW_DEFAULT = "gradledev-tests-user";
	private static final String REPO_USER_DEFAULT = "gradledev-tests-user";
	private static final String SNAPSHOTS_REPO_SUFFIX_DEFAULT = "snapshots";
	private static final String RELEASE_REPO_SUFFIX_DEFAULT = "releases";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga";
	
	private ApgMavenPublishDsl extData;
	private String remoteRepoBaseUrl = REPO_BASE_URL_DEFAULT;
	private String remoteRelease = RELEASE_REPO_SUFFIX_DEFAULT;
	private String remoteSnapshot = SNAPSHOTS_REPO_SUFFIX_DEFAULT;
	private String remoteRepoUser = REPO_USER_DEFAULT;
	private String remoteRepoPw = REPO_PW_DEFAULT;
	
	
	public RemoteRepo(ApgMavenPublishDsl extData) {
		super();
		this.extData = extData;
	}
	public void configure() {
		final Logger logger = extData.project.getLogger();
		PublishingExtension publishingExtension = extData.project.getExtensions().getByType(PublishingExtension.class);
		// Configure Repository Location
		logger.info(
				"Configuring publish repository to be a maven type remote repository hosted at: " + remoteRepoBaseUrl);
		RepositoryHandler repositories = publishingExtension.getRepositories();
		repositories.maven(m -> {
			m.setUrl(remoteRepoBaseUrl + "/" + (extData.getVersion().endsWith("SNAPSHOT") ? remoteSnapshot : remoteRelease));
			m.setName("artifactoryMavenRepo");
			PasswordCredentials credentials = m.getCredentials();
			credentials.setUsername(remoteRepoUser);
			credentials.setPassword(remoteRepoPw);
		});
		extData.configureMavenPublication("RemoteJavaMaven",publishingExtension);

	}

	
	public String getRemoteRepoBaseUrl() {
		return remoteRepoBaseUrl;
	}
	public void setRemoteRepoBaseUrl(String remoteRepoBaseUrl) {
		this.remoteRepoBaseUrl = remoteRepoBaseUrl;
	}
	public String getRemoteRelease() {
		return remoteRelease;
	}
	public void setRemoteRelease(String remoteRelease) {
		this.remoteRelease = remoteRelease;
	}
	public String getRemoteSnapshot() {
		return remoteSnapshot;
	}
	public void setRemoteSnapshot(String remoteSnapshot) {
		this.remoteSnapshot = remoteSnapshot;
	}
	public String getRemoteRepoUser() {
		return remoteRepoUser;
	}
	public void setRemoteRepoUser(String remoteRepoUser) {
		this.remoteRepoUser = remoteRepoUser;
	}
	public String getRemoteRepoPw() {
		return remoteRepoPw;
	}
	public void setRemoteRepoPw(String remoteRepoPw) {
		this.remoteRepoPw = remoteRepoPw;
	}
	public void setRootData(ApgMavenPublishDsl apgPublishDsl) {
		this.extData = apgPublishDsl;
		
	}

}
