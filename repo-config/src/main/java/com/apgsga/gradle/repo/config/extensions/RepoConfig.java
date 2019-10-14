package com.apgsga.gradle.repo.config.extensions;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;

import com.apgsga.gradle.repo.extensions.LocalRepo;
import com.apgsga.gradle.repo.extensions.RemoteRepo;

public class RepoConfig {
	
	public RepoConfig(Project project) {
		mavenLocal(project);
		apgRemote(project);
		mavenCentral(project);
	}

	private void mavenCentral(Project project) {
		project.getRepositories().add(project.getRepositories().mavenCentral());
		
	}

	private void apgRemote(Project project) {
		RemoteRepo remote = project.getExtensions().findByType(RemoteRepo.class);
		RepositoryHandler repositories = project.getRepositories();
		repositories.maven(m -> {
			m.setUrl(remote.getRepoBaseUrl());
			m.setName(remote.getRepoName());
			PasswordCredentials credentials = m.getCredentials();
			credentials.setUsername(remote.getUser());
			credentials.setPassword(remote.getPassword());
		});
	}

	private void mavenLocal(Project project) {
		LocalRepo localRepo = project.getExtensions().findByType(LocalRepo.class);
		MavenArtifactRepository mavenLocal = project.getRepositories().mavenLocal();
		mavenLocal.setUrl(localRepo.getRepoBaseUrl());
		project.getRepositories().add(mavenLocal);
	}
}
