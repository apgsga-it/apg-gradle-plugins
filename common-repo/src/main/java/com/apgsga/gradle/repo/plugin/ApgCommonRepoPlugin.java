package com.apgsga.gradle.repo.plugin;

import com.apgsga.gradle.repo.extensions.ApgRepo;
import com.apgsga.gradle.repo.extensions.RepoType;
import com.apgsga.gradle.repo.extensions.ReposImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@NonNullApi
public class ApgCommonRepoPlugin implements Plugin<Project> {

	public static final String COMMMON_REPO_PLUGIN_NAME = "apgRepos";

	private static final String REPO_NAMES_JSON_FILENAME = "repoNames.json";

	private Project project;

	@Override
	public void apply(final Project project) {
		this.project = project;
		final ExtensionContainer ext = project.getExtensions();
		ReposImpl reposImpl = ext.create(COMMMON_REPO_PLUGIN_NAME, ReposImpl.class, project);

		RepoNamesBean repoNames = loadRepoNames();
		repoNames.repos.forEach(r -> {
			r.keySet().forEach(key -> {
				String remoteRepoBaseUrl = key.equals(RepoType.LOCAL) ? project.getRepositories().mavenLocal().getUrl().getPath() : repoNames.repoBaseUrl;
				reposImpl.getRepositories().put(key, new ApgRepo(remoteRepoBaseUrl, r.get(key), repoNames.repoUserName, repoNames.repoUserPwd));
			});
		});
	}

	private RepoNamesBean loadRepoNames() {
		RepoNamesBean rnb = null;
		try {
			rnb = new ObjectMapper().readerFor(RepoNamesBean.class).readValue(getRepoNameResource().getFile());
		} catch (IOException e) {
			throw new RuntimeException("Problem while deserializing " + REPO_NAMES_JSON_FILENAME + ". Original esxception was: " + e.getMessage());
		}
		return rnb;
	}

	private Resource getRepoNameResource() {
		String gradleHome = project.getGradle().getGradleUserHomeDir().getAbsolutePath();
		FileSystemResourceLoader loader = new FileSystemResourceLoader();
		String repoNamesJsonFilePath = "file://" + gradleHome + File.separator + REPO_NAMES_JSON_FILENAME;
		Resource repoNamesJsonAsResource = loader.getResource(repoNamesJsonFilePath);
		Assert.isTrue(repoNamesJsonAsResource.exists(), REPO_NAMES_JSON_FILENAME + " file not found! repoNamesJsonFilePath = " + repoNamesJsonFilePath);
		return repoNamesJsonAsResource;
	}
}