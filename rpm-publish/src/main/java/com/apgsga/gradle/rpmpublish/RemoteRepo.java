package com.apgsga.gradle.rpmpublish;

public class RemoteRepo {

	private static final String REPO_PW_DEFAULT = "dev1234";
	private static final String REPO_USER_DEFAULT = "dev";
	private static final String RPM_REPO_DEFAULT = "yumdigiflexsnaprepo";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga";

	private String remoteRepoBaseUrl = REPO_BASE_URL_DEFAULT;
	private String rpmRepo = RPM_REPO_DEFAULT;
	private String remoteRepoUser = REPO_USER_DEFAULT;
	private String remoteRepoPw = REPO_PW_DEFAULT;
	private Boolean remotePublish = true;

	public RemoteRepo() {
	}

	public String getRemoteRepoBaseUrl() {
		return remoteRepoBaseUrl;
	}

	public void setRemoteRepoBaseUrl(String remoteRepoBaseUrl) {
		this.remoteRepoBaseUrl = remoteRepoBaseUrl;
	}

	public String getRemoteRepoUser() {
		return remoteRepoUser;
	}

	public void setRemoteRepoUser(String remoteRepoUser) {
		this.remoteRepoUser = remoteRepoUser;
	}

	public String getRemoteRepoPw() {
		return remoteRepoPw;
	}

	public void setRemoteRepoPw(String remoteRepoPw) {
		this.remoteRepoPw = remoteRepoPw;
	}

	public Boolean getRemotePublish() {
		return remotePublish;
	}

	public void setRemotePublish(Boolean remotePublish) {
		this.remotePublish = remotePublish;
	}

	public String getRpmRepo() {
		return rpmRepo;
	}

	public void setRpmRepo(String rpmRepo) {
		this.rpmRepo = rpmRepo;
	}

	@Override
	public String toString() {
		return "RemoteRepo [remoteRepoBaseUrl=" + remoteRepoBaseUrl + ", rpmRepo=" + rpmRepo + ", remoteRepoUser="
				+ remoteRepoUser + ", remotePublish=" + remotePublish + "]";
	}

	

}
