package com.apgsga.gradle.publish.extension;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

public class ApgGenericPublish {

	protected final Project project;
	private String artefactFile;
	private boolean publishLocal = false;
	private boolean publishRemote = false;

	@Inject
	public ApgGenericPublish(final Project project) {
		this.project = project;
	}

	public String getArtefactFile() {
		return artefactFile;
	}

	public void setArtefactFile(String artefactFile) {
		this.artefactFile = artefactFile;
	}

	public void log() {
		Logger logger = project.getLogger();
		logger.info("Logging ApgRpmPublishConfig:");
		logger.info(toString());

	}

	public void artifactory() {
		publishRemote = true;
	}

	public void local() {
		publishLocal = true;
	}

	public boolean isPublishLocal() {
		return publishLocal;
	}

	public boolean isPublishRemote() {
		return publishRemote;
	}

}
