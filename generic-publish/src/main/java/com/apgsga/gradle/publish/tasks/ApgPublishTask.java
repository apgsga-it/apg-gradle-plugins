package com.apgsga.gradle.publish.tasks;

import java.io.File;

import com.apgsga.gradle.repo.extensions.RepoNames;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

import com.apgsga.gradle.publish.extension.ApgGenericPublish;
import com.apgsga.gradle.repo.extensions.LocalRepo;
import com.apgsga.gradle.repo.extensions.RemoteRepo;
import com.apgsga.gradle.repo.extensions.Repo;
import com.apgsga.gradle.repository.RepositoryBuilderFactory;
import com.apgsga.gradle.repository.Repository;
import com.apgsga.gradle.repository.RepositoryBuilder;

public class ApgPublishTask extends DefaultTask {

	private RegularFileProperty artefactFile;

	public ApgPublishTask() {
		this.artefactFile = getProject().getObjects().fileProperty();
	}

	@InputFile
	public File getArtefactFile() {
		return artefactFile.getAsFile().get();
	}

	public void setArtefactFile(RegularFileProperty artefactFile) {
		this.artefactFile = artefactFile;
	}

	@TaskAction
	public void publish() {
		Logger logger = getLogger();
		logger.info("Starting ApgRpmPublishTask");
		ApgGenericPublish config = getProject().getExtensions().findByType(ApgGenericPublish.class);
		LocalRepo localConfig = getProject().getExtensions().findByType(LocalRepo.class);
		RemoteRepo remoteConfig = getProject().getExtensions().findByType(RemoteRepo.class);
		assert config != null;
		config.log();
		File theFile = artefactFile.getAsFile().get();
		configure(localConfig, config.isPublishLocal(), theFile.getName()).upload(theFile.getName(), theFile);
		configure(remoteConfig, config.isPublishRemote(), theFile.getName()).upload(theFile.getName(), theFile);
		logger.info("ApgRpmPublishTask done.");

	}

	private Repository configure(Repo repo, boolean publish, String filename) {
		logger.info("Are following repo(s) configured for publish:  " + repo.getDefaultRepoNames() + " -> " + publish);
		RepositoryBuilder builder = RepositoryBuilderFactory.createFor(publish ? null : repo.getRepoBaseUrl());
        builder.setTargetRepo(getMavenRepoName(repo,filename));
		builder.setUsername(repo.getUser());
		builder.setPassword(repo.getPassword());
		return builder.build();
	}

	private String getMavenRepoName(Repo repo, String filename) {

		// TODO JHE : to be deleted, need to temporarily output couple of info
		System.out.println("(getMavenRepoName) repo.getRepoBaseUrl() = " + repo.getRepoBaseUrl());
		System.out.println("(getMavenRepoName) repo.getDefaultRepoNames() = " + repo.getDefaultRepoNames());
		System.out.println("(getMavenRepoName) filename = " + filename);

		String mavenRepo = "RPM";
		if(repo.getDefaultRepoNames().containsKey(RepoNames.LOCAL)) {
			// TODO JHE : to be deleted, need to temporarily output couple of info
			System.out.println("(getMavenRepoName) Dealing with local repo");
			mavenRepo = repo.getDefaultRepoNames().get(RepoNames.LOCAL);
		}
		else {
			// TODO JHE : to be deleted, need to temporarily output couple of info
			System.out.println("(getMavenRepoName) Dealing with remote repo");
			mavenRepo = filename.toLowerCase().endsWith("rpm") ? repo.getDefaultRepoNames().get(RepoNames.RPM) : repo.getDefaultRepoNames().get(RepoNames.MAVEN_RELEASE);
		}

		// TODO JHE : to be deleted, need to temporarily output couple of info
		System.out.println("(getMavenRepoName) mavenRepo = " + mavenRepo);
		return mavenRepo;
	}
}
