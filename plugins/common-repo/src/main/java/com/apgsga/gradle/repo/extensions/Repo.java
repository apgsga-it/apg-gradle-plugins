package com.apgsga.gradle.repo.extensions;

public interface Repo {

	String getRepoBaseUrl();

	void setRepoBaseUrl(String repoBaseUrl);

	String getUser();

	void setUser(String user);

	String getPassword();

	void setPassword(String password);
	
	void setRepoName(String name);

	String getRepoName();
	
	void log();
}