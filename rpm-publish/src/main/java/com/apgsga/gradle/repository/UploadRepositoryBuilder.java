package com.apgsga.gradle.repository;

public interface UploadRepositoryBuilder {

	UploadRepository build();

	String getTargetRepo();

	UploadRepositoryBuilder setTargetRepo(String targetRepo);

	String getBaseUrl();

	UploadRepositoryBuilder setBaseUrl(String baseUrl);

	UploadRepositoryBuilder setUsername(String username);

	String getUsername();

	UploadRepositoryBuilder setPassword(String password);

	String getPassword();

}