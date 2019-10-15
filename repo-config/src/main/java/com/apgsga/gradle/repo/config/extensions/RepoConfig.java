package com.apgsga.gradle.repo.config.extensions;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;

import com.apgsga.gradle.repo.extensions.LocalRepo;
import com.apgsga.gradle.repo.extensions.RemoteRepo;

public class RepoConfig {
	
	private Project project;
	
	public RepoConfig(Project project) {
		this.project = project;
	}

	public void mavenCentral() {
		project.getRepositories().add(project.getRepositories().mavenCentral());
		
	}

	public void artifactory() {
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

	public void local() {
		LocalRepo localRepo = project.getExtensions().findByType(LocalRepo.class);
		MavenArtifactRepository mavenLocal = project.getRepositories().mavenLocal();
		mavenLocal.setUrl(localRepo.getRepoBaseUrl());
		project.getRepositories().add(mavenLocal);
	}
	
	public void mavenLocal() {
		project.getRepositories().add(project.getRepositories().mavenLocal());
	}

}