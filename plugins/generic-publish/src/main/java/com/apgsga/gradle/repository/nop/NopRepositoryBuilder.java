package com.apgsga.gradle.repository.nop;

import com.apgsga.gradle.repository.Repository;
import com.apgsga.gradle.repository.RepositoryBuilder;

public class NopRepositoryBuilder implements RepositoryBuilder {
	
	public static RepositoryBuilder NOP = new NopRepositoryBuilder();

	@Override
	public Repository build() {
		return NopRepository.NOP;
	}

	@Override
	public RepositoryBuilder setTargetRepo(String targetRepo) {
		return this;
	}

	@Override
	public RepositoryBuilder setBaseUrl(String baseUrl) {
		return this;
	}

	@Override
	public RepositoryBuilder setUsername(String username) {
		return this;
	}

	@Override
	public RepositoryBuilder setPassword(String password) {
		return this;
	}

}
