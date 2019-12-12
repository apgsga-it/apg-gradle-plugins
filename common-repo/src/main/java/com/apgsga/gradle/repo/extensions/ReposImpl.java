package com.apgsga.gradle.repo.extensions;

import com.fasterxml.jackson.annotation.JsonProperty;
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

public class ReposImpl implements  Repos {

    private Project project;

    private Map<RepoType,Repo> repositories = Maps.newHashMap();

    @JsonProperty
    private String repoUserName;

    @JsonProperty
    private String repoUserPwd;

    @JsonProperty
    private String repoBaseUrl;

    @JsonProperty
    private List<Map<RepoType,String>> repos;

    public ReposImpl() {
    }

    public ReposImpl(Project project, ReposImpl repos) {
        this.project = project;
        repos.getRepos().forEach(r -> {
			r.keySet().forEach(key -> {
				String remoteRepoBaseUrl = key.equals(RepoType.LOCAL) ? project.getRepositories().mavenLocal().getUrl().getPath() : repos.getRepoBaseUrl();
				repositories.put(key, new ApgRepo(remoteRepoBaseUrl, r.get(key), repos.getRepoUserName(), repos.getRepoUserPwd()));
			});
		});
    }

    // TODO JHE : Verify which getter/setter are really necessary.

    public List<Map<RepoType, String>> getRepos() {
        return repos;
    }

    public void setRepos(List<Map<RepoType, String>> repos) {
        this.repos = repos;
    }

    public String getRepoUserName() {
        return repoUserName;
    }

    public void setRepoUserName(String repoUserName) {
        this.repoUserName = repoUserName;
    }

    public String getRepoUserPwd() {
        return repoUserPwd;
    }

    public void setRepoUserPwd(String repoUserPwd) {
        this.repoUserPwd = repoUserPwd;
    }

    public String getRepoBaseUrl() {
        return repoBaseUrl;
    }

    public void setRepoBaseUrl(String repoBaseUrl) {
        this.repoBaseUrl = repoBaseUrl;
    }

    @Override
    public Repo get(RepoType repoType) {
        return repositories.get(repoType);
    }

    @Override
    public void set(RepoType repoType, Repo repo) {
        this.repositories.put(repoType,repo);
    }

    @Override
    public Repo getRepoFor(String archiveName) {
        return archiveName.toLowerCase().endsWith("rpm") ? get(RepoType.RPM) : archiveName.toLowerCase().endsWith("zip") ? get(RepoType.ZIP) : get(RepoType.MAVEN_RELEASE);
    }

    public void config(RepoType repotype, Map<RepoProperties,String> properties) {
        if(properties.containsKey(RepoProperties.REPO_NAME.name))
            this.repositories.get(repotype).setRepoName(properties.get(RepoProperties.REPO_NAME.name));
        if(properties.containsKey(RepoProperties.REPO_BASE_URL.name))
            this.repositories.get(repotype).setRepoBaseUrl(properties.get(RepoProperties.REPO_BASE_URL.name));
        if(properties.containsKey(RepoProperties.REPO_USER.name))
            this.repositories.get(repotype).setUser(properties.get(RepoProperties.REPO_USER.name));
        if(properties.containsKey(RepoProperties.REPO_PASSWORD.name))
            this.repositories.get(repotype).setPassword(properties.get(RepoProperties.REPO_PASSWORD.name));
    }
}
