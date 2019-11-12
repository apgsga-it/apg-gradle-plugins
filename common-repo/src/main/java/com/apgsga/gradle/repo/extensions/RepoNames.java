package com.apgsga.gradle.repo.extensions;

public enum RepoNames {
	
	GENERIC("GENERIC"),
	MAVEN("MAVEN"),
	RPM("RPM"),
	SNAPSHOT("SNAPSHOT"),
	ZIP("ZIP"),
	LOCAL("LOCAL");
	
	private String repoName;
	
	RepoNames(String repoName) {
		this.repoName= repoName;
	}
}
