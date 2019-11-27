package com.apgsga.gradle.repo.extensions;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.springframework.util.Assert;

import java.util.Map;

public class RemoteRepo extends AbstractRepo {

	private static final String REPO_USER_NAME_KEY = "repoUserName";

	private static final String REPO_USER_PWD_KEY = "repoUserPwd";

	private static final String REPO_BASE_URL_KEY = "repoBaseUrl";

	private String repoUserDefault;
	private String repoPwdDefault;

	public RemoteRepo(Project project) {
		super(project);
		initRepoUserAndPwd();
		initRepoBaseUrl();
	}

	private void initRepoBaseUrl() {
		String baseUrl = (String) getRepoNameJsonAsMap().get(REPO_BASE_URL_KEY);
		Assert.notNull(baseUrl, "repoBaseUrl not found within " + REPO_NAMES_JSON_FILENAME);
		repoBaseUrl = baseUrl;
	}

	private void initRepoUserAndPwd() {
		String user = (String) getRepoNameJsonAsMap().get(REPO_USER_NAME_KEY);
		String pwd = (String) getRepoNameJsonAsMap().get(REPO_USER_PWD_KEY);
		Assert.notNull(user, "repoUserName not found within " + REPO_NAMES_JSON_FILENAME);
		Assert.notNull(pwd, "repoUserPwd not found within " + REPO_NAMES_JSON_FILENAME);
		repoUserDefault = user;
		repoPwdDefault = pwd;
	}

	@Override
	public String getDefaultRepoBaseUrl() {
		return repoBaseUrl;
	}

	@Override
	public String getDefaultUser() {
		return repoUserDefault;
	}

	@Override
	public String getDefaultPassword() {
		return repoPwdDefault;
	}

	@Override
	public void log() {
		Logger logger = project.getLogger();
		logger.info("Remote Repository Configuration is:" );
		logger.info(super.toString());
	}

}