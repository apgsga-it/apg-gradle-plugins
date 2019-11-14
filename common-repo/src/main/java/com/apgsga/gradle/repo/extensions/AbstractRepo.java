package com.apgsga.gradle.repo.extensions;

import org.gradle.api.Project;

public abstract class AbstractRepo implements Repo {
	private String repoBaseUrl;
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
	

	@Override
	public String toString() {
		return "[repoBaseUrl=" + getRepoBaseUrl() + ", user=" + getUser() + ", password=xxxxxx"
				+ ", repoNames=" + getDefaultRepoNames() + ", project=" + project + "]";
	}

	
	
}
