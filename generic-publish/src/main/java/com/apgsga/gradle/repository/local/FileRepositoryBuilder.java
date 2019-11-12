package com.apgsga.gradle.repository.local;

import java.io.File;


import com.apgsga.gradle.repository.Repository;
import com.apgsga.gradle.repository.RepositoryBuilder;
import com.google.common.base.Preconditions;

public class FileRepositoryBuilder implements RepositoryBuilder {
	public static RepositoryBuilder create(String baseUrl) {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		builder.setBaseUrl(baseUrl);
		return builder;
	}

	private String baseUrl;
	private String targetRepo;

	@Override
	public Repository build() {
		File parentDir = new File(baseUrl);
		Preconditions.checkState(parentDir.exists(), "Base Repository Directory doesn't exist: ", baseUrl);
		Preconditions.checkState(parentDir.isDirectory(), "Base Repository Directory is not a Directory: ", baseUrl);
		Preconditions.checkState(parentDir.canWrite(), "Base Repository Directory is not writable: ", baseUrl);

		File repoDir = new File(parentDir, targetRepo);
		if (!repoDir.exists()) {
			repoDir.mkdirs();
		} else {
			Preconditions.checkState(repoDir.isDirectory(), "Repo exists but isn't a directory: ", targetRepo);

		}
		return new FileRepository(repoDir);

	}

	@Override
	public RepositoryBuilder setTargetRepo(String targetRepo) {
		this.targetRepo = targetRepo;
		return this;
	}

	@Override
	public RepositoryBuilder setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}
	
	// Null Ops

	@Override
	public RepositoryBuilder setUsername(String username) {
		return this;
	}

	@Override
	public RepositoryBuilder setPassword(String password) {
		return this;
	}


}
