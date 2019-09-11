package com.apgsga.gradle.mavenpublish;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;

public class ApgMavenPublishDsl {

	private static final String VERSION_DEFAULT = "0.1-SNAPSHOT";
	private static final String GROUP_ID_DEFAULT = "com.apgsga";

	protected final Project project;
	private String artefactId;
	private String groupId = GROUP_ID_DEFAULT;
	private String version = VERSION_DEFAULT;
	private LocalRepo localRepo = new LocalRepo(this);
	private RemoteRepo remoteRepo = new RemoteRepo(this);

	@Inject
	public ApgMavenPublishDsl(final Project project) {
		this.project = project;
	}

	protected void configureMavenPublication(String name, PublishingExtension publishingExtension) {
		// Configure Publications
		PublicationContainer publications = publishingExtension.getPublications();
		MavenPublication mavenPublication = publications.create(name, MavenPublication.class);
		mavenPublication.from(project.getComponents().getByName("java"));
		if (artefactId != null) {
			mavenPublication.setArtifactId(artefactId);
		}
		mavenPublication.setGroupId(groupId);
		mavenPublication.setVersion(version);
	}

	public LocalRepo getLocalRepo() {
		return localRepo;
	}

	public void localRepo(Action<? super LocalRepo> action) {
		action.execute(localRepo);
		localRepo.setRootData(this);

	}

	public RemoteRepo getRemoteRepo() {
		return remoteRepo;
	}

	public void remoteRepo(Action<? super RemoteRepo> action) {
		action.execute(remoteRepo);
		remoteRepo.setRootData(this);

	}

	public Project getProject() {
		return project;
	}

	public String getArtefactId() {
		return artefactId;
	}

	public void setArtefactId(String artefactId) {
		this.artefactId = artefactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
