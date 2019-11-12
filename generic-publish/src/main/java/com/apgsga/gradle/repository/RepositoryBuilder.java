package com.apgsga.gradle.repository;

public interface RepositoryBuilder {

	Repository build();

	RepositoryBuilder setTargetRepo(String targetRepo);

	RepositoryBuilder setBaseUrl(String baseUrl);

	RepositoryBuilder setUsername(String username);

	RepositoryBuilder setPassword(String password);

}