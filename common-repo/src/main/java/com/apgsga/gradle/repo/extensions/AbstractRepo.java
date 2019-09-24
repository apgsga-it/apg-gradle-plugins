package com.apgsga.gradle.repo.extensions;

import org.gradle.api.Project;

public abstract class AbstractRepo implements Repo {
	private String repoBaseUrl = getDefaultRepoBaseUrl();
	private String repoName = getDefaultRepoName();
	private String releaseRepoName = getDefaultReleaseRepoName();
	private String snapshotRepoName = getDefaultSnapshotRepoName();
	private String user = getDefaultUser();
	private String password = getDefaultPassword();
	protected Project project; 

	public AbstractRepo(Project project) {
		this.project = project;
	}

	public String getRepoBaseUrl() {
		return repoBaseUrl;
	}

	public void setRepoBaseUrl(String repoBaseUrl) {
		this.repoBaseUrl = repoBaseUrl;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String getDefaultUser() {
		return "";
	}

	@Override
	public String getDefaultPassword() {
		return "";
	}
	

	public String getReleaseRepoName() {
		return releaseRepoName;
	}

	public void setReleaseRepoName(String releaseRepoName) {
		this.releaseRepoName = releaseRepoName;
	}

	public String getSnapshotRepoName() {
		return snapshotRepoName;
	}

	public void setSnapshotRepoName(String snapshotRepoName) {
		this.snapshotRepoName = snapshotRepoName;
	}

	@Override
	public String toString() {
		return "[repoBaseUrl=" + repoBaseUrl + ", repoName=" + repoName + ", releaseRepoName="
				+ releaseRepoName + ", snapshotRepoName=" + snapshotRepoName + ", user=" + user + ", password=xxxxxx"
				 + ", project=" + project + "]";
	}

	
	
}
