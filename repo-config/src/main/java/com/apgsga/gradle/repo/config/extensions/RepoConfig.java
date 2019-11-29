package com.apgsga.gradle.repo.config.extensions;

import com.apgsga.gradle.repo.extensions.LocalRepo;
import com.apgsga.gradle.repo.extensions.RemoteRepo;
import com.apgsga.gradle.repo.extensions.RepoType;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;

public class RepoConfig {
	
	private Project project;
	
	public RepoConfig(Project project) {
		this.project = project;
	}

	public void mavenCentral() {
		project.getRepositories().add(project.getRepositories().mavenCentral());
		
	}
	// TODO (jhe, che, 29.11 Api Discussion) : Example change , code does'nt compile
	public void mavenApgCentral() {

		RepoType repoName = RepoType.MAVEN;
		RemoteRepo remote = project.getExtensions().findByType(RemoteRepo.class);
		project.getLogger().info("Using Artifactory with the following configuration");
		remote.log();
		RepositoryHandler repositories = project.getRepositories();
		repositories.maven(m -> {
			m.setUrl(remote.getRepoBaseUrl() + "/" + remote.getDefaultRepoNames().get(repoName));
			m.setName(repoName.asString());
			PasswordCredentials credentials = m.getCredentials();
			credentials.setUsername(remote.getUser());
			credentials.setPassword(remote.getPassword());
		});
	}

	public void local() {
		LocalRepo localRepo = project.getExtensions().findByType(LocalRepo.class);
		project.getLogger().info("Using Local Maven Repo with the following configuration");
		localRepo.log();
		MavenArtifactRepository mavenLocal = project.getRepositories().mavenLocal();
		mavenLocal.setUrl(localRepo.getRepoBaseUrl());
		project.getRepositories().add(mavenLocal);
	}
	
	public void mavenLocal() {
		project.getRepositories().add(project.getRepositories().mavenLocal());
	}

}