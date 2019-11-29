package com.apgsga.gradle.publish.tasks;

import com.apgsga.gradle.publish.extension.ApgGenericPublish;
import com.apgsga.gradle.repo.extensions.LocalRepo;
import com.apgsga.gradle.repo.extensions.RemoteRepo;
import com.apgsga.gradle.repo.extensions.Repo;
import com.apgsga.gradle.repo.extensions.RepoType;
import com.apgsga.gradle.repository.Repository;
import com.apgsga.gradle.repository.RepositoryBuilder;
import com.apgsga.gradle.repository.RepositoryBuilderFactory;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

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
		getLogger().info("Are following repo(s) configured for publish:  " + repo.getDefaultRepoNames() + " -> " + publish);
		RepositoryBuilder builder = RepositoryBuilderFactory.createFor(publish ? repo.getRepoBaseUrl() : null);
        builder.setTargetRepo(getMavenRepoName(repo,filename));
		builder.setUsername(repo.getUser());
		builder.setPassword(repo.getPassword());
		return builder.build();
	}

	private String getMavenRepoName(Repo repo, String filename) {
		return filename.toLowerCase().endsWith("rpm") ? repo.getDefaultRepoNames().get(RepoType.RPM) : repo.getDefaultRepoNames().get(RepoType.MAVEN_RELEASE);
	}
}
