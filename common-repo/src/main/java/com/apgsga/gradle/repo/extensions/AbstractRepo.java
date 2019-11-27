package com.apgsga.gradle.repo.extensions;

import com.google.common.collect.Maps;
import groovy.json.JsonSlurper;
import org.gradle.api.Project;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepo implements Repo {

	protected static final String REPO_NAMES_JSON_FILENAME = "repoNames.json";

	private static final String REPOS_KEY = "repos";

	private String user = getDefaultUser();
	private String password = getDefaultPassword();
	protected Project project;
	protected Map<RepoNames,String> repoNames;
	protected String repoBaseUrl;

	public AbstractRepo(Project project) {
		this.project = project;
		Map repoNameAsJson = getRepoNameJsonAsMap();
		initRepoNames(repoNameAsJson);
	}

	private void initRepoNames(Map repoNameAsJson) {
		List<Map> repos = (List<Map>) repoNameAsJson.get(REPOS_KEY);
		repoNames = Maps.newHashMap();
		repoNames.put(RepoNames.MAVEN_RELEASE, getRepoName(RepoNames.MAVEN_RELEASE, repos));
		repoNames.put(RepoNames.MAVEN_SNAPSHOT, getRepoName(RepoNames.MAVEN_SNAPSHOT, repos));
		repoNames.put(RepoNames.RPM, getRepoName(RepoNames.RPM, repos));
		repoNames.put(RepoNames.ZIP, getRepoName(RepoNames.ZIP, repos));
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

	protected Map getRepoNameJsonAsMap() {
		try {
			JsonSlurper slurper = new JsonSlurper();
			return (Map) slurper.parse(getRepoNameResource().getFile());
		} catch (IOException e) {
			throw new RuntimeException("common-repo Plugin couldn't be correctly instanciated, probably because " + REPO_NAMES_JSON_FILENAME + " couldn't be found. Original Exception Message was: " + e.getMessage());
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

	@Override
	public Map<RepoNames, String> getDefaultRepoNames() {
		return repoNames;
	}

	// TODO (che, 9.10 ) : Implement this pattern preferably for all defaults
	public String getRepoBaseUrl() {

		return repoBaseUrl == null ? getDefaultRepoBaseUrl() : repoBaseUrl;
	}

	public void setRepoBaseUrl(String repoBaseUrl) {
		this.repoBaseUrl = repoBaseUrl;
	}

	public String getUser() {
		return user == null ? getDefaultUser() : user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password == null ? getDefaultPassword() : password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String getDefaultUser() {
		return "";
	}

	@Override
	public String getDefaultPassword() {
		return "";
	}
	

	@Override
	public String toString() {
		return "[repoBaseUrl=" + getRepoBaseUrl() + ", user=" + getUser() + ", password=xxxxxx"
				+ ", repoNames=" + getDefaultRepoNames() + ", project=" + project + "]";
	}

	
	
}
