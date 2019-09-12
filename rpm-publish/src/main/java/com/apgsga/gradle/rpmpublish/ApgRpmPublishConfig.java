package com.apgsga.gradle.rpmpublish;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

public class ApgRpmPublishConfig {

	protected final Project project;
	private File rpmFile;
	private LocalRepo localRepo = new LocalRepo();
	private RemoteRepo remoteRepo = new RemoteRepo();

	@Inject
	public ApgRpmPublishConfig(final Project project) {
		this.project = project;
	}
	
	public File getRpmFile() {
		return rpmFile;
	}

	public void setRpmFile(File rpmFile) {
		this.rpmFile = rpmFile;
	}


	public void logConfig() {
		Logger logger = project.getLogger(); 
		logger.info("Logging ApgRpmPublishConfig:");
		logger.info(toString()); 
		
	}
	public LocalRepo getLocalRepo() {
		return localRepo;
	}

	public void localRepo(Action<? super LocalRepo> action) {
		action.execute(localRepo);
	}

	public RemoteRepo getRemoteRepo() {
		return remoteRepo;
	}

	public void remoteRepo(Action<? super RemoteRepo> action) {
		action.execute(remoteRepo);
	}

	public Project getProject() {
		return project;
	}

	@Override
	public String toString() {
		return "ApgRpmPublishConfig [project=" + project + ", rpmFile=" + rpmFile + ", localRepo=" + localRepo
				+ ", remoteRepo=" + remoteRepo + "]";
	}

}
