package com.apgsga.gradle.repo.extensions;

import com.google.common.collect.Maps;
import groovy.json.JsonSlurper;
import org.gradle.api.Project;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ApgRepo implements Repo {

	private String user;
	private String password;
	private String repoName;
	private String repoBaseUrl;

	// TODO JHE: pass correct parameter ... we probably want to have multiple constructors
	public ApgRepo(String repoBaseUrl, String repoName, String user, String password) {
		this.repoBaseUrl = repoBaseUrl;
		this.repoName = repoName;
		this.user = user;
		this.password = password;
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
		// TODO JHE
		System.out.println("Here you can see log for ApgRepo ... for now we'll get the toString()");
		System.out.println(toString());
	}


	@Override
	public String toString() {
		return "[repoBaseUrl=" + getRepoBaseUrl() + ", user=" + getUser() + ", password=xxxxxx"
				+ ", repoName=" + getRepoName() + "]";
	}

	
	
}
