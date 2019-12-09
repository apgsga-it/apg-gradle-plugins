package com.apgsga.gradle.publish.tasks;

import java.io.File;

import com.apgsga.gradle.repo.extensions.RepoType;
import com.apgsga.gradle.repo.extensions.Repos;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

import com.apgsga.gradle.publish.extension.ApgGenericPublish;
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
		assert config != null;
		Repos repos = (Repos) getProject().getExtensions().findByName("apgReposConfig");
		assert repos != null;
		config.log();
		File theFile = artefactFile.getAsFile().get();
		configure(repos.get(RepoType.LOCAL), config.isPublishLocal(), theFile.getName()).upload(theFile.getName(), theFile);
		configure(getMavenRepo(repos, theFile.getName()), config.isPublishRemote(), theFile.getName()).upload(theFile.getName(), theFile);
		logger.info("ApgRpmPublishTask done.");
	}

	private Repository configure(Repo repo, boolean publish, String filename) {
		getLogger().info("Is following repo configured for publish:  " + repo.getRepoName() + " -> " + publish);
		RepositoryBuilder builder = RepositoryBuilderFactory.createFor(publish ? repo.getRepoBaseUrl() : null);
        builder.setTargetRepo(repo.getRepoName());
		builder.setUsername(repo.getUser());
		builder.setPassword(repo.getPassword());
		return builder.build();
	}

	private Repo getMavenRepo(Repos repos, String filename) {
		return filename.toLowerCase().endsWith("rpm") ? repos.get(RepoType.RPM) : filename.toLowerCase().endsWith("zip") ? repos.get(RepoType.ZIP) : repos.get(RepoType.MAVEN_RELEASE);
	}
}