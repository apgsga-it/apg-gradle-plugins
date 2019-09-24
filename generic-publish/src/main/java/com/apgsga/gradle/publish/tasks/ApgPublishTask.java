package com.apgsga.gradle.publish.tasks;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

import com.apgsga.gradle.publish.extension.ApgGenericPublish;
import com.apgsga.gradle.repo.extensions.LocalRepo;
import com.apgsga.gradle.repo.extensions.RemoteRepo;
import com.apgsga.gradle.repo.extensions.Repo;
import com.apgsga.gradle.repository.RepositoryBuilderFactory;
import com.apgsga.gradle.repository.UploadRepository;
import com.apgsga.gradle.repository.UploadRepositoryBuilder;
import com.apgsga.gradle.repository.nop.NopUploadRepository;

public class ApgPublishTask extends DefaultTask {

	@TaskAction
	public void publish() {
		Logger logger = getLogger();
		logger.info("Starting ApgRpmPublishTask");
		ApgGenericPublish config = getProject().getExtensions().findByType(ApgGenericPublish.class);
		LocalRepo localConfig = getProject().getExtensions().findByType(LocalRepo.class);
		RemoteRepo remoteConfig = getProject().getExtensions().findByType(RemoteRepo.class);
		config.log();
		File artefactFile = new File(config.getArtefactFile());
		configure(localConfig,config.isPublishLocal()).upload(artefactFile.getName(), artefactFile);
		configure(remoteConfig, config.isPublishRemote()).upload(artefactFile.getName(), artefactFile);
		logger.info("ApgRpmPublishTask done.");

	}

	private UploadRepository configure(Repo repo, boolean publish) {
		if (!publish) {
			Logger logger = getLogger();
			logger.info("Skipping Publication for: " + repo.getClass() + " type");
			return NopUploadRepository.NOP;
		}
		UploadRepositoryBuilder builder = RepositoryBuilderFactory.createFor(repo.getRepoBaseUrl());
		builder.setTargetRepo(repo.getRepoName());
		builder.setUsername(repo.getUser());
		builder.setPassword(repo.getPassword());
		return builder.build();

	}

}
