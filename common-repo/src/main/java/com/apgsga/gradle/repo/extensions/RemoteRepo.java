package com.apgsga.gradle.repo.extensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import groovy.json.JsonSlurper;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import com.google.common.collect.Maps;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;


public class RemoteRepo extends AbstractRepo {

//	private static final String RELEASE_REPO_NAME_DEFAULT = "release-functionaltest";
//	private static final String SNAPSHOT_REPO_NAME_DEFAULT = "snapshot-functionaltest";
//	private static final String RPM_REPO_NAME_DEFAULT = "rpm-functionaltest";
	private static final String REPO_USER_DEFAULT = "gradledev-tests-user";
	private static final String REPO_PW_DEFAULT = "gradledev-tests-user";
	private static final String REPO_BASE_URL_DEFAULT = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga";
	private static Map<String,String> repoNames;

	static {
		try {
			ClassPathResource cpr = new ClassPathResource("repoNames.json");
			Assert.isTrue(cpr.exists(), "repoNames.json file not found!");
			JsonSlurper slurper = new JsonSlurper();
			Map parse = (Map) slurper.parse(cpr.getInputStream());
			List<Map> repos = (List<Map>) parse.get("repos");
			initRepoNames(repos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void initRepoNames(List<Map> repos) {
		repoNames = Maps.newHashMap();
		repoNames.put(RepoNames.MAVEN_RELEASE.toString(), getRepoTypeName(RepoNames.MAVEN_RELEASE, repos));
		repoNames.put(RepoNames.MAVEN_SNAPSHOT.toString(), getRepoTypeName(RepoNames.MAVEN_SNAPSHOT, repos));
		repoNames.put(RepoNames.RPM.toString(), getRepoTypeName(RepoNames.RPM, repos));
		repoNames.put(RepoNames.ZIP.toString(), getRepoTypeName(RepoNames.ZIP, repos));
	}

	private static String getRepoTypeName(RepoNames repo, List<Map> repos) {
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
	
	public RemoteRepo(Project project) {
		super(project);
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
