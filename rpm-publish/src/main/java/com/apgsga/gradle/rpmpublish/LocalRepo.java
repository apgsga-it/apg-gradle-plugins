package com.apgsga.gradle.rpmpublish;

public class LocalRepo {

	private static final String LOCAL_REPO_DIR_DEFAULT = "build/maventestrepo";
	private String localRepoDir = LOCAL_REPO_DIR_DEFAULT;
	private Boolean publishLocal = false;

	public LocalRepo() {
	}

	public Boolean getPublishLocal() {
		return publishLocal;
	}

	public void setPublishLocal(Boolean publishLocal) {
		this.publishLocal = publishLocal;
	}

	public String getLocalRepoDir() {
		return localRepoDir;
	}

	public void setLocalRepoDir(String localRepoDir) {
		this.localRepoDir = localRepoDir;
	}

	@Override
	public String toString() {
		return "LocalRepo [localRepoDir=" + localRepoDir + ", publishLocal=" + publishLocal + "]";
	}

}
