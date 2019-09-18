package com.apgsga.gradle.publish.extension;

public interface Repo {

	String getRepoBaseUrl();

	void setRepoBaseUrl(String repoBaseUrl);

	String getRepoName();

	void setRepoName(String repoName);

	String getUser();

	void setUser(String user);

	String getPassword();

	void setPassword(String password);

	Boolean getPublish();

	void setPublish(Boolean publish);
	
	String getDefaultRepoBaseUrl();
	
	String getDefaultRepoName();
	
	String getDefaultUser(); 
	
	String getDefaultPassword(); 
	
	Boolean getDefaultPublish(); 

}