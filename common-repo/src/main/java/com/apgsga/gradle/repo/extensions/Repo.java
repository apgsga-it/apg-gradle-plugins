package com.apgsga.gradle.repo.extensions;

public interface Repo {

	String getRepoBaseUrl();

	void setRepoBaseUrl(String repoBaseUrl);

	String getRepoName();

	void setRepoName(String repoName);
	
	String getReleaseRepoName();

	void setReleaseRepoName(String repoName);
	
	String getSnapshotRepoName();

	void setSnapshotRepoName(String repoName);

	String getUser();

	void setUser(String user);

	String getPassword();

	void setPassword(String password);
	
	String getDefaultRepoBaseUrl();
	
	String getDefaultRepoName();
	
	String getDefaultReleaseRepoName();
	
	String getDefaultSnapshotRepoName();
	
	String getDefaultUser(); 
	
	String getDefaultPassword(); 
	
	void log(); 


}