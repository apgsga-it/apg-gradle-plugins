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
		configure(rt);
	}

	public void local() {
		createLocalRepoDirectories();
		configure(RepoType.LOCAL);
	}

	public void mavenLocal() {
		PublishingExtension publishingExtension = project.getExtensions().getByType(PublishingExtension.class);
		project.getRepositories().add(project.getRepositories().mavenLocal());
		configureMavenPublication("mavenLocal", publishingExtension);
	}

    private void configure(RepoType rt) {
        final Logger logger = project.getLogger();
        PublishingExtension publishingExtension = project.getExtensions().getByType(PublishingExtension.class);
        Repos repos = project.getExtensions().findByType(ReposImpl.class);
        // Configure Repository Location
        logger.info(
                "Configuring publish repository to be a maven type remote repository hosted at: " + repos.get(rt).getRepoBaseUrl());
        RepositoryHandler repositories = publishingExtension.getRepositories();
        repositories.maven(m -> {
            m.setName(rt.asString());
            m.setUrl(repos.get(rt).getRepoBaseUrl() + "/" + (getVersion().endsWith("SNAPSHOT") ? repos.get(rt).getDefaultRepoNames().get(RepoType.MAVEN_SNAPSHOT) : repos.get(rt).getDefaultRepoNames().get(RepoType.MAVEN_RELEASE)));
            // In case of LocalRepo, well, we'll just get nulls
			PasswordCredentials credentials = m.getCredentials();
			credentials.setUsername(repos.get(rt).getUser());
			credentials.setPassword(repos.get(rt).getPassword());
        });
        configureMavenPublication(rt.equals(RepoType.LOCAL) ? "LocalJavaMaven" : "RemoteJavaMaven", publishingExtension);
    }

	private void createLocalRepoDirectories() {
		Repos repos = project.getExtensions().findByType(ReposImpl.class);
		File baseDir = new File(repos.get(RepoType.LOCAL).getRepoBaseUrl());
		repos.get(RepoType.LOCAL).getDefaultRepoNames().forEach((repoNames, s) -> {
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
