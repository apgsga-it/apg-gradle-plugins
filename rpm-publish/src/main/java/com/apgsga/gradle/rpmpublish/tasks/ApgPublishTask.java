package com.apgsga.gradle.rpmpublish.tasks;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

import com.apgsga.gradle.repository.RepositoryBuilderFactory;
import com.apgsga.gradle.repository.UploadRepository;
import com.apgsga.gradle.repository.UploadRepositoryBuilder;
import com.apgsga.gradle.repository.nop.NopUploadRepository;
import com.apgsga.gradle.rpmpublish.extension.ApgPublishConfig;
import com.apgsga.gradle.rpmpublish.extension.Repo;

public class ApgPublishTask extends DefaultTask {

	@TaskAction
	public void publish() {
		Logger logger = getLogger();
		logger.info("Starting ApgRpmPublishTask");
		ApgPublishConfig config = getProject().getExtensions().findByType(ApgPublishConfig.class);
		config.logConfig();
		File artefactFile = new File(config.getArtefactFile());
		configure(config.getLocalRepo()).upload(artefactFile.getName(), artefactFile);
		configure(config.getRemoteRepo()).upload(artefactFile.getName(), artefactFile);
		logger.info("ApgRpmPublishTask done.");

	}

	private UploadRepository configure(Repo repo) {
		if (!repo.getPublish()) {
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
