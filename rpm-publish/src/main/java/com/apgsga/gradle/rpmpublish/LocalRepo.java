package com.apgsga.gradle.rpmpublish;

import java.io.File;
import java.net.URI;

import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.publish.PublishingExtension;

public class LocalRepo {

	private static final String LOCAL_REPO_DIR_DEFAULT = "build/maventestrepo";

	private ApgRpmPublishDsl extData;
	private String localRepoDir = LOCAL_REPO_DIR_DEFAULT;

	public LocalRepo(ApgRpmPublishDsl extData) {
		super();
		this.extData = extData;
	}

	public void configure() {
		final Logger logger = extData.project.getLogger();

		PublishingExtension publishingExtension = extData.project.getExtensions().getByType(PublishingExtension.class);
		// Configure Repository Location
		URI localRepoDirURI = createLocalRepoDirectory();
		logger.info("Configuring publish repository to be a maven type hosted at local URI: "
				+ localRepoDirURI.toASCIIString());
		RepositoryHandler repositories = publishingExtension.getRepositories();
		repositories.maven(m -> {
			m.setName("localMavenRepo");
			m.setUrl(localRepoDirURI);
		});
		extData.configureMavenPublication("LocalJavaMaven", publishingExtension);

	}

	public void logDefaults() {

	}

	public void logActual() {

	}

	private URI createLocalRepoDirectory() {
		File repoDir = new File(localRepoDir);
		repoDir.mkdirs();
		return repoDir.toURI();

	}

	public String getLocalRepoDir() {
		return localRepoDir;
	}

	public void setLocalRepoDir(String localRepoDir) {
		this.localRepoDir = localRepoDir;
	}

	public void setRootData(ApgRpmPublishDsl apgPublishDsl) {
		this.extData = apgPublishDsl;

	}

}
