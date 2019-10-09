package com.apgsga.gradle.publish.extension;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.logging.Logger;

public class ApgGenericPublish {

	private final Project project;

	private RegularFileProperty artefactFile;
	private boolean publishLocal = false;
	private boolean publishRemote = false;

	@Inject
	public ApgGenericPublish(final Project project) {
		this.project = project;
		this.artefactFile = project.getObjects().fileProperty();
	}

	public File getArtefactFile() {
		return artefactFile.get().getAsFile();
	}

	public void setArtefactFile(File artefactFile) {
		this.artefactFile.set(artefactFile);
	}
	
	public void setArtefactFile(RegularFileProperty artefactFile) {
		this.artefactFile = artefactFile;
	}

	public RegularFileProperty getArtefactFileProvider() {
		return artefactFile;
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

	@Override
	public String toString() {
		return "ApgGenericPublish [artefactFile=" + artefactFile + ", publishLocal=" + publishLocal + ", publishRemote="
				+ publishRemote + "]";
	}

}
