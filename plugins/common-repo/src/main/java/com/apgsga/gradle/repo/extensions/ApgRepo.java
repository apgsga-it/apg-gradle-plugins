package com.apgsga.gradle.repo.extensions;

public class ApgRepo implements Repo {

	private String user;
	private String password;
	private String repoName;
	private String repoBaseUrl;

	public ApgRepo(String repoBaseUrl, String repoName, String user, String password) {
		this.repoBaseUrl = repoBaseUrl;
		this.repoName = repoName;
		this.user = user;
		this.password = password;
	}

	public ApgRepo(String repoBaseUrl, String repoName) {
		this(repoBaseUrl,repoName,null,null);
	}

	// TODO (che, 9.10 ) : Implement this pattern preferably for all defaults
	public String getRepoBaseUrl() {
		return repoBaseUrl;
	}

	public void setRepoBaseUrl(String repoBaseUrl) {
		this.repoBaseUrl = repoBaseUrl;
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
	public void setRepoName(String name) {
		this.repoName = name;
	}

	@Override
	public String getRepoName() {
		return repoName;
	}

	@Override
	public void log() {
		System.out.println(getRepoName() + " repo has the below configuration");
		System.out.println(toString());
	}

	@Override
	public String toString() {
		return "[repoBaseUrl=" + getRepoBaseUrl() + ", user=" + getUser() + ", password=xxxxxx"
				+ ", repoName=" + getRepoName() + "]";
	}
}
