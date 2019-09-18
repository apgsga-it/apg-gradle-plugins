package com.apgsga.gradle.publish.extension;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

public class ApgPublishConfig {

	protected final Project project;
	private String artefactFile;
	private Repo localRepo = new LocalRepo();
	private Repo remoteRepo = new RemoteRepo();

	@Inject
	public ApgPublishConfig(final Project project) {
		this.project = project;
	}

	public String getArtefactFile() {
		return artefactFile;
	}

	public void setArtefactFile(String artefactFile) {
		this.artefactFile = artefactFile;
	}

	public void logConfig() {
		Logger logger = project.getLogger();
		logger.info("Logging ApgRpmPublishConfig:");
		logger.info(toString());

	}

	public Repo getLocalRepo() {
		return localRepo;
	}

	public void localRepo(Action<? super Repo> action) {
		action.execute(localRepo);
	}

	public Repo getRemoteRepo() {
		return remoteRepo;
	}

	public void remoteRepo(Action<? super Repo> action) {
		action.execute(remoteRepo);
	}

	public Project getProject() {
		return project;
	}

	@Override
	public String toString() {
		return "ApgPublishConfig [project=" + project + ", artefactFile=" + artefactFile + ", localRepo=" + localRepo
				+ ", remoteRepo=" + remoteRepo + "]";
	}

}
