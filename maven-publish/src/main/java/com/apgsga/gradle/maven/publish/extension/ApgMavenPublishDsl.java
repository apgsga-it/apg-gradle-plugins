package com.apgsga.gradle.maven.publish.extension;

import java.io.File;
import java.net.URI;

import javax.inject.Inject;

import com.apgsga.gradle.repo.extensions.Repo;
import com.apgsga.gradle.repo.extensions.RepoNames;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.logging.Logger;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;

import com.apgsga.gradle.repo.extensions.LocalRepo;
import com.apgsga.gradle.repo.extensions.RemoteRepo;

public class ApgMavenPublishDsl {

	private static final String VERSION_DEFAULT = "0.1-SNAPSHOT";
	private static final String GROUP_ID_DEFAULT = "com.apgsga";

	protected final Project project;
	private String artefactId;
	private String groupId = GROUP_ID_DEFAULT;
	private String version = VERSION_DEFAULT;

	@Inject
	public ApgMavenPublishDsl(final Project project) {
		this.project = project;
	}
	

	public Project getProject() {
		return project;
	}

	public String getArtefactId() {
		return artefactId;
	}

	public void setArtefactId(String artefactId) {
		this.artefactId = artefactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	public void log() {
		Logger logger = project.getLogger();
		logger.info("Logging ApgRpmPublishConfig:");
		logger.info(toString());

	}

	public void artifactory() {
		final Logger logger = project.getLogger();
		PublishingExtension publishingExtension = project.getExtensions().getByType(PublishingExtension.class);
		RemoteRepo repoConfig = project.getExtensions().findByType(RemoteRepo.class);
		// Configure Repository Location
		logger.info(
				"Configuring publish repository to be a maven type remote repository hosted at: " + repoConfig.getRepoBaseUrl());
		RepositoryHandler repositories = publishingExtension.getRepositories();
		repositories.maven(m -> {
			m.setUrl(getRepoUrl(repoConfig));
			m.setName("artifactoryMavenRepo");
			PasswordCredentials credentials = m.getCredentials();
			credentials.setUsername(repoConfig.getUser());
			credentials.setPassword(repoConfig.getPassword());
		});
	    configureMavenPublication("RemoteJavaMaven",publishingExtension);

	}

	public void local() {
		final Logger logger = project.getLogger();
		PublishingExtension publishingExtension = project.getExtensions().getByType(PublishingExtension.class);
		LocalRepo localConfig = project.getExtensions().findByType(LocalRepo.class);
		// Configure Repository Location
		logger.info(
				"Configuring publish repository to be a maven type remote repository hosted at: " + localConfig.getRepoBaseUrl());
		createLocalRepoDirectories();
		RepositoryHandler repositories = publishingExtension.getRepositories();
		repositories.maven(m -> {
			m.setName("localMavenRepo");
			m.setUrl(getRepoUrl(localConfig));
		});
		configureMavenPublication("LocalJavaMaven", publishingExtension);
	}

	private String getRepoUrl(Repo repo) {
		return (repo.getRepoBaseUrl() + "/" + (getVersion().endsWith("SNAPSHOT") ? repo.getDefaultRepoNames().get(RepoNames.MAVEN_SNAPSHOT) : repo.getDefaultRepoNames().get(RepoNames.MAVEN_RELEASE)));
	}
	
	private void createLocalRepoDirectories() {
		LocalRepo localConfig = project.getExtensions().findByType(LocalRepo.class);
		File baseDir = new File(localConfig.getRepoBaseUrl());
		localConfig.getDefaultRepoNames().forEach((repoNames, s) -> {
			File repoDir = new File(baseDir,s);
			repoDir.mkdirs();
		});
	}

	private void configureMavenPublication(String name, PublishingExtension publishingExtension) {
		// Configure Publications
		PublicationContainer publications = publishingExtension.getPublications();
		MavenPublication mavenPublication = publications.create(name, MavenPublication.class);
		mavenPublication.from(project.getComponents().getByName("java"));
		if (artefactId != null) {
			mavenPublication.setArtifactId(artefactId);
		}
		mavenPublication.setGroupId(groupId);
		mavenPublication.setVersion(version);
	}

}
