package com.apgsga.gradle.repo.extensions;

import com.google.common.collect.Maps;
import groovy.json.JsonSlurper;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RemoteRepo extends AbstractRepo {

	private static final String REPO_NAMES_JSON_FILENAME = "repoNames.json";

	private static final String REPO_BASE_URL_KEY = "repoBaseUrl";

	private static final String REPO_USER_NAME_KEY = "repoUserName";

	private static final String REPO_USER_PWD_KEY = "repoUserPwd";

	private static final String REPOS_KEY = "repos";

	private static Map<String,String> repoNames;
	private String repoUserDefault;
	private String repoPwdDefault;
	private String repoBaseUrl;

	// TODO JHE: Not sure we want to throw an exception here ... but if the JSON isn't found , well, we can't really go further...
	public RemoteRepo(Project project) throws Exception {
		super(project);
		Map repoNameAsJson = getRepoNameJsonAsMap();
		initRepoNames(repoNameAsJson);
		initRepoUserAndPwd(repoNameAsJson);
		initRepoBaseUrl(repoNameAsJson);
	}

	private Map getRepoNameJsonAsMap() throws Exception {
		try {
			JsonSlurper slurper = new JsonSlurper();
			return (Map) slurper.parse(getRepoNameResource().getFile());
		} catch (IOException e) {
			throw e;
		}
	}

	private Resource getRepoNameResource() {
		String gradleHome = project.getGradle().getGradleUserHomeDir().getAbsolutePath();
		FileSystemResourceLoader loader = new FileSystemResourceLoader();
		String repoNamesJsonFilePath = "file://" + gradleHome + File.separator + REPO_NAMES_JSON_FILENAME;
		Resource repoNamesJsonAsResource = loader.getResource(repoNamesJsonFilePath);
		Assert.isTrue(repoNamesJsonAsResource.exists(), "repoNames.json file not found! repoNamesJsonFilePath = " + repoNamesJsonFilePath);
		return repoNamesJsonAsResource;
	}

	private void initRepoBaseUrl(Map repoNameAsJson) {
		String baseUrl = (String) repoNameAsJson.get(REPO_BASE_URL_KEY);
		Assert.notNull(baseUrl, "repoBaseUrl not found within " + REPO_NAMES_JSON_FILENAME);
		repoBaseUrl = baseUrl;
	}

	private void initRepoUserAndPwd(Map repoNameAsJson) {
		String user = (String) repoNameAsJson.get(REPO_USER_NAME_KEY);
		String pwd = (String) repoNameAsJson.get(REPO_USER_PWD_KEY);
		Assert.notNull(user, "repoUserName not found within " + REPO_NAMES_JSON_FILENAME);
		Assert.notNull(pwd, "repoUserPwd not found within " + REPO_NAMES_JSON_FILENAME);
		repoUserDefault = user;
		repoPwdDefault = pwd;
	}

	private void initRepoNames(Map repoNameAsJson) {
		List<Map> repos = (List<Map>) repoNameAsJson.get(REPOS_KEY);
		repoNames = Maps.newHashMap();
		repoNames.put(RepoNames.MAVEN_RELEASE.toString(), getRepoName(RepoNames.MAVEN_RELEASE, repos));
		repoNames.put(RepoNames.MAVEN_SNAPSHOT.toString(), getRepoName(RepoNames.MAVEN_SNAPSHOT, repos));
		repoNames.put(RepoNames.RPM.toString(), getRepoName(RepoNames.RPM, repos));
		repoNames.put(RepoNames.ZIP.toString(), getRepoName(RepoNames.ZIP, repos));
	}

	private String getRepoName(RepoNames repo, List<Map> repos) {
		// TODO JHE: Mmh, not very efficient, replace the below with a Lambda
		String repoName = "";
		for(Map m : repos) {
			if(m.containsKey(repo.toString())) {
				repoName = String.valueOf(m.get(repo.toString()));
				break; //break loop
			}
		}
		return repoName;
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

	@Override
	public Map<String, String> getDefaultRepoNames() {
		return repoNames;
	}

}
