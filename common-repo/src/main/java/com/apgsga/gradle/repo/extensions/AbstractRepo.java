package com.apgsga.gradle.repo.extensions;

import org.gradle.api.Project;

public abstract class AbstractRepo implements Repo {
	private String repoBaseUrl;
	private String repoName;
	private String releaseRepoName = getDefaultReleaseRepoName();
	private String snapshotRepoName = getDefaultSnapshotRepoName();
	private String user = getDefaultUser();
	private String password = getDefaultPassword();
	protected Project project; 

	public AbstractRepo(Project project) {
		this.project = project;
	}

	// TODO (che, 9.10 ) : Implement this pattern preferably for all defaults
	public String getRepoBaseUrl() {
		return repoBaseUrl == null ? getDefaultRepoBaseUrl() : repoBaseUrl; 
	}

	public void setRepoBaseUrl(String repoBaseUrl) {
		this.repoBaseUrl = repoBaseUrl;
	}

	public String getRepoName() {
		return repoName == null ? getDefaultRepoName() : repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getUser() {
		return user == null ? getDefaultUser() : user;
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
		return releaseRepoName == null ? getDefaultReleaseRepoName() : releaseRepoName;
	}

	public void setReleaseRepoName(String releaseRepoName) {
		this.releaseRepoName = releaseRepoName;
	}

	public String getSnapshotRepoName() {
		return snapshotRepoName == null ? getDefaultSnapshotRepoName() : snapshotRepoName;
	}

	public void setSnapshotRepoName(String snapshotRepoName) {
		this.snapshotRepoName = snapshotRepoName;
	}

	@Override
	public String toString() {
		return "[repoBaseUrl=" + getRepoBaseUrl() + ", repoName=" + getRepoName() + ", releaseRepoName="
				+ getReleaseRepoName() + ", snapshotRepoName=" + getSnapshotRepoName() + ", user=" + getUser() + ", password=xxxxxx"
				 + ", project=" + project + "]";
	}

	
	
}
