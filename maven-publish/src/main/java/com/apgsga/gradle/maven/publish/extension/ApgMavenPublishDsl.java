package com.apgsga.gradle.maven.publish.extension;

import com.apgsga.gradle.repo.extensions.RepoType;
import com.apgsga.gradle.repo.extensions.Repos;
import com.apgsga.gradle.repo.extensions.ReposImpl;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.logging.Logger;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;

import javax.inject.Inject;
import java.io.File;

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

	public void artifactory(RepoType p_rt) {
		// TODO JHE: mmhh, default Maven? really? Maybe we want another approach to configure where to publish
		RepoType rt = p_rt != null ? p_rt : RepoType.MAVEN;
		RepositoryHandler repositories = getPublishingExtension().getRepositories();
		Repos repos = getRepos();
		repositories.maven(m -> {
			m.setName(rt.asString());
			m.setUrl(repos.get(rt).getRepoBaseUrl() + "/" + (getVersion().endsWith("SNAPSHOT") ? repos.get(RepoType.MAVEN_SNAPSHOT).getRepoName() : repos.get(RepoType.MAVEN_RELEASE).getRepoName()));
			// In case of LocalRepo, well, we'll just get nulls
			PasswordCredentials credentials = m.getCredentials();
			credentials.setUsername(repos.get(rt).getUser());
			credentials.setPassword(repos.get(rt).getPassword());
		});
		configure(rt);
	}

	private PublishingExtension getPublishingExtension() {
		return project.getExtensions().getByType(PublishingExtension.class);
	}

	private Repos getRepos() {
		return project.getExtensions().findByType(ReposImpl.class);
	}

	public void local() {
		configure(RepoType.LOCAL);
	}

	public void mavenLocal() {
		project.getRepositories().add(project.getRepositories().mavenLocal());
		configureMavenPublication("mavenLocal", getPublishingExtension());
	}

    private void configure(RepoType rt) {
		project.getLogger().info("Configuring publish repository to be a maven type remote repository hosted at: " + getRepos().get(rt).getRepoBaseUrl());
        configureMavenPublication(rt.equals(RepoType.LOCAL) ? "LocalJavaMaven" : "RemoteJavaMaven", getPublishingExtension());
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
