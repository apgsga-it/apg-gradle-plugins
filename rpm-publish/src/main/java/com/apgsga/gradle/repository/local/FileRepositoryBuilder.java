package com.apgsga.gradle.repository.local;

import java.io.File;

import org.gradle.internal.impldep.com.google.api.client.util.Preconditions;

import com.apgsga.gradle.repository.UploadRepository;
import com.apgsga.gradle.repository.UploadRepositoryBuilder;

public class FileRepositoryBuilder implements UploadRepositoryBuilder {
	public static UploadRepositoryBuilder create(String baseUrl) {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		builder.setBaseUrl(baseUrl);
		return builder;
	}

	private String baseUrl;
	private String targetRepo;

	@Override
	public UploadRepository build() {
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
	public String getTargetRepo() {
		return targetRepo;
	}

	@Override
	public UploadRepositoryBuilder setTargetRepo(String targetRepo) {
		this.targetRepo = targetRepo;
		return this;
	}

	@Override
	public String getBaseUrl() {
		return baseUrl;
	}

	@Override
	public UploadRepositoryBuilder setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

}
