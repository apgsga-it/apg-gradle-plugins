package com.apgsga.gradle.publish.tasks;

import java.io.File;

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
import com.apgsga.gradle.repository.UploadRepository;
import com.apgsga.gradle.repository.UploadRepositoryBuilder;

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
		config.log();
		File theFile = artefactFile.getAsFile().get();
		configure(localConfig, config.isPublishLocal()).upload(theFile.getName(), theFile);
		configure(remoteConfig, config.isPublishRemote()).upload(theFile.getName(), theFile);
		logger.info("ApgRpmPublishTask done.");

	}

	private UploadRepository configure(Repo repo, boolean publish) {
		UploadRepositoryBuilder builder = RepositoryBuilderFactory.createFor(repo.getRepoBaseUrl());
		builder.setTargetRepo(repo.getRepoName());
		builder.setUsername(repo.getUser());
		builder.setPassword(repo.getPassword());
		return builder.build();

	}

}
