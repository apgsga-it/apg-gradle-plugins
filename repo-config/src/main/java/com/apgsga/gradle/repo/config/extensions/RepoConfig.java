package com.apgsga.gradle.repo.config.extensions;

import com.apgsga.gradle.repo.extensions.RepoType;
import com.apgsga.gradle.repo.extensions.Repos;
import com.apgsga.gradle.repo.plugin.ApgCommonRepoPlugin;
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

	public void artifactory(RepoType p_rt) {
		RepoType repoType = p_rt != null ? p_rt : RepoType.MAVEN_RELEASE;
		Repos repos = getReposExtension();
		project.getLogger().info("Using Artifactory with the following configuration");
		repos.get(repoType).log();
		RepositoryHandler repositories = project.getRepositories();
		repositories.maven(m -> {
			m.setUrl(repos.get(repoType).getRepoBaseUrl() + "/" + repos.get(repoType).getRepoName());
			m.setName(repoType.asString());
			PasswordCredentials credentials = m.getCredentials();
			credentials.setUsername(repos.get(repoType).getUser());
			credentials.setPassword(repos.get(repoType).getPassword());
		});
	}

	public void local() {
		Repos repos = getReposExtension();
		project.getLogger().info("Using Local Maven Repo with the following configuration");
		repos.get(RepoType.LOCAL).log();
		MavenArtifactRepository mavenLocal = project.getRepositories().mavenLocal();
		mavenLocal.setUrl(repos.get(RepoType.LOCAL).getRepoBaseUrl());
		project.getRepositories().add(mavenLocal);
	}

	private Repos getReposExtension() {
		return (Repos) project.getExtensions().findByName(ApgCommonRepoPlugin.COMMMON_REPO_EXTENSION_NAME);
	}
	
	public void mavenLocal() {
		project.getRepositories().add(project.getRepositories().mavenLocal());
	}

}