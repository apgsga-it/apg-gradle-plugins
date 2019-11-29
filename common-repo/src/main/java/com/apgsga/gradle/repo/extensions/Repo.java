package com.apgsga.gradle.repo.extensions;

public interface Repo {

	String getRepoBaseUrl();

	String getUser();

	void setUser(String user);

	String getPassword();

	void setPassword(String password);

}