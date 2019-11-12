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
import com.apgsga.gradle.repo.extensions.RepoNames;
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
		configure(localConfig, config.isPublishLocal()).upload(theFile.getName(), theFile);
		configure(remoteConfig, config.isPublishRemote()).upload(theFile.getName(), theFile);
		logger.info("ApgRpmPublishTask done.");

	}

	private Repository configure(Repo repo, boolean publish) {
		RepositoryBuilder builder = RepositoryBuilderFactory.createFor(publish ? null : repo.getRepoBaseUrl());
        // JHE: Really, always within RPM Repo? Or should we provide the target Repo as parameter?
        builder.setTargetRepo(repo.getRepoNames().get(RepoNames.RPM.toString()));
		builder.setUsername(repo.getUser());
		builder.setPassword(repo.getPassword());
		return builder.build();

	}

}
