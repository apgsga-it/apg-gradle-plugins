package com.apgsga.gradle.repository;

import com.apgsga.gradle.repository.artifactory.RemoteRepositoryBuilder;
import com.apgsga.gradle.repository.local.FileRepositoryBuilder;
import com.apgsga.gradle.repository.nop.NopRepositoryBuilder;

public class RepositoryBuilderFactory {
	
	public static RepositoryBuilder createFor(String baseUrl) {
		if (baseUrl == null)  {
			return NopRepositoryBuilder.NOP;
		}
		if (baseUrl.startsWith("http")) {
			return RemoteRepositoryBuilder.create(baseUrl); 
		}
		return FileRepositoryBuilder.create(baseUrl); 
	}

	public static RepositoryBuilder createFor(String baseUrl, String repoName, String user, String passwd) {
		final RepositoryBuilder repoBuilder = createFor(baseUrl);
		repoBuilder.setTargetRepo(repoName);
		repoBuilder.setUsername(user);
		repoBuilder.setPassword(passwd);
		return repoBuilder;
	}

	public static RepositoryBuilder createFor(String baseUrl, String repoName) {
		final RepositoryBuilder repoBuilder = createFor(baseUrl);
		repoBuilder.setTargetRepo(repoName);
		return repoBuilder;
	}

}
