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

	private static final String REPO_USER_DEFAULT = "gradledev-tests-user";
	private static final String REPO_PW_DEFAULT = "gradledev-tests-user";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga";
	private static final String REPO_NAMES_JSON_FILENAME = "repoNames.json";
	private static Map<String,String> repoNames;

	public RemoteRepo(Project project) {
		super(project);

		try {
			String gradleHome = project.getGradle().getGradleHomeDir().getAbsolutePath();
			FileSystemResourceLoader loader = new FileSystemResourceLoader();
			String repoNamesJsonFilePath = "file://" + gradleHome + File.separator + REPO_NAMES_JSON_FILENAME;
			Resource repoNamesJsonAsResource = loader.getResource(repoNamesJsonFilePath);
			Assert.isTrue(repoNamesJsonAsResource.exists(), "repoNames.json file not found! repoNamesJsonFilePath = " + repoNamesJsonFilePath);
			JsonSlurper slurper = new JsonSlurper();
			Map parse = (Map) slurper.parse(repoNamesJsonAsResource.getFile());
			List<Map> repos = (List<Map>) parse.get("repos");
			initRepoNames(repos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initRepoNames(List<Map> repos) {
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
		return REPO_BASE_URL_DEFAULT;
	}

	@Override
	public String getDefaultUser() {
		return REPO_USER_DEFAULT; 
	}

	@Override
	public String getDefaultPassword() {
		return REPO_PW_DEFAULT;
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
