package com.apgsga.gradle.publish.extension;

public class RemoteRepo extends AbstractRepo {

	private static final String REPO_PW_DEFAULT = "dev1234";
	private static final String REPO_USER_DEFAULT = "dev";
	private static final String REPO_DEDFAULT = "yumdigiflexsnaprepo";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga";

	public RemoteRepo() {
	}

	@Override
	public String getDefaultRepoBaseUrl() {
		return REPO_BASE_URL_DEFAULT;
	}

	@Override
	public String getDefaultRepoName() {
		return REPO_DEDFAULT;
	}

	@Override
	public Boolean getDefaultPublish() {
		return true;
	}

	@Override
	public String getDefaultUser() {
		return REPO_USER_DEFAULT; 
	}

	@Override
	public String getDefaultPassword() {
		return REPO_PW_DEFAULT; 
	}


	

}
