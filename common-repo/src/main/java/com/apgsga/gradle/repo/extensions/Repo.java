package com.apgsga.gradle.repo.extensions;

import java.util.Map;

public interface Repo {

	String getRepoBaseUrl();

	void setRepoBaseUrl(String repoBaseUrl);

	String getUser();

	void setUser(String user);

	String getPassword();

	void setPassword(String password);
	
	String getDefaultRepoBaseUrl();
	
	Map<String, String> getRepoNames();
	
	String getDefaultUser(); 
	
	String getDefaultPassword(); 
	
	void log(); 


}