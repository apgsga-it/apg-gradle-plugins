package com.apgsga.gradle.repository.nop;

import com.apgsga.gradle.repository.UploadRepository;
import com.apgsga.gradle.repository.UploadRepositoryBuilder;

public class NopRepositoryBuilder implements UploadRepositoryBuilder {
	
	public static UploadRepositoryBuilder NOP = new NopRepositoryBuilder();

	@Override
	public UploadRepository build() {
		return NopUploadRepository.NOP;
	}

	@Override
	public String getTargetRepo() {
		return "";
	}

	@Override
	public UploadRepositoryBuilder setTargetRepo(String targetRepo) {
		return this;
	}

	@Override
	public String getBaseUrl() {
		return "";
	}

	@Override
	public UploadRepositoryBuilder setBaseUrl(String baseUrl) {
		return this;
	}

	@Override
	public UploadRepositoryBuilder setUsername(String username) {
		return this;
	}

	@Override
	public String getUsername() {
		return "";
	}

	@Override
	public UploadRepositoryBuilder setPassword(String password) {
		return this;
	}

	@Override
	public String getPassword() {
		return "";
	}

}
