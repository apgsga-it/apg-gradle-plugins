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

public class ReposImpl implements  Repos {

    private Project project;

    private Map<RepoType,Repo> repositories;

    public ReposImpl(Project project, Map<RepoType,Repo> repositories) {
        this.project = project;
        this.repositories = repositories;
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
