package com.apgsga.gradle.publish.extension;

public abstract class AbstractRepo implements Repo {
	private String repoBaseUrl = getDefaultRepoBaseUrl();
	private String repoName = getDefaultRepoName();
	private String user = getDefaultUser();
	private String password = getDefaultPassword();
	private Boolean publish = getDefaultPublish();

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

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
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
		return "AbstractRepo [repoBaseUrl=" + repoBaseUrl + ", repoName=" + repoName + ", user=" + user + ", password="
				+ password + ", publish=" + publish + "]";
	}

	
}
