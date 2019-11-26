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
		RepositoryBuilder builder = RepositoryBuilderFactory.createFor(publish ? null : repo.getRepoBaseUrl());
        builder.setTargetRepo(getMavenRepoName(repo,filename));
		builder.setUsername(repo.getUser());
		builder.setPassword(repo.getPassword());
		return builder.build();
	}

	private String getMavenRepoName(Repo repo, String filename) {
		String mavenRepo = "RPM";
		if(repo.getDefaultRepoNames().containsKey(RepoNames.LOCAL)) {
			//TODO JHE : remove this
			System.out.println("getMAvenRepoName, dealing with a LOCAL Repo , repo.getDefaultRepoNames() = " + repo.getDefaultRepoNames());
			mavenRepo = repo.getDefaultRepoNames().get(RepoNames.LOCAL);
		}
		else {
			//TODO JHE : remove this
			System.out.println("getMAvenRepoName, dealing with a REMOTE Repo , repo.getDefaultRepoNames() = " + repo.getDefaultRepoNames());
			mavenRepo = filename.toLowerCase().endsWith("rpm") ? repo.getDefaultRepoNames().get(RepoNames.RPM) : repo.getDefaultRepoNames().get(RepoNames.MAVEN_RELEASE);
		}

		// TODO JHE: Remove this
		System.out.println("within getMavenRepoName, mavenRepo = " + mavenRepo);
		return mavenRepo;
	}
}
