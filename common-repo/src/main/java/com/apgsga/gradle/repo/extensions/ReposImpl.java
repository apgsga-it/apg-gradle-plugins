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

    private static final String REPO_NAMES_JSON_FILENAME = "repoNames.json";

    private static final String REPOS_KEY = "repos";

    private static final String REPO_USER_NAME = "repoUserName";

    private static final String REPO_USER_PWD = "repoUserPwd";

    private static final String REPO_BASE_URL = "repoBaseUrl";

    private Project project;

    private Map<RepoType,Repo> repositories;

    public ReposImpl(Project project) {
        this.project = project;
        initRepositories();
    }

    //JHE: do that as static init, would be cool, but impossible because of local repo name which comes from project variable.
    // TODO JHE: will be done with IT-35150
    private void initRepositories() {
        Map repoNameAsJson = getRepoNameJsonAsMap();
        List<Map> repos = (List<Map>) repoNameAsJson.get(REPOS_KEY);
        repositories = Maps.newHashMap();

        String repoUser = (String) repoNameAsJson.get(REPO_USER_NAME);
        String repoPwd = (String) repoNameAsJson.get(REPO_USER_PWD);
        String remoteRepoBaseUrl = (String) repoNameAsJson.get(REPO_BASE_URL);

        Repo localRepo = new ApgRepo(project.getRepositories().mavenLocal().getUrl().getPath(),getRepoName(RepoType.LOCAL,repos));
        Repo zipRepo = new ApgRepo(remoteRepoBaseUrl,getRepoName(RepoType.ZIP,repos),repoUser,repoPwd);
        Repo rpmRepo = new ApgRepo(remoteRepoBaseUrl,getRepoName(RepoType.RPM,repos),repoUser,repoPwd);
        Repo mavenRepo = new ApgRepo(remoteRepoBaseUrl,getRepoName(RepoType.MAVEN,repos),repoUser,repoPwd);
        Repo mavenSnapshotRepo = new ApgRepo(remoteRepoBaseUrl,getRepoName(RepoType.MAVEN_SNAPSHOT,repos),repoUser,repoPwd);
        Repo mavenReleaseRepo = new ApgRepo(remoteRepoBaseUrl,getRepoName(RepoType.MAVEN_RELEASE,repos),repoUser,repoPwd);
        Repo javaDistRepo = new ApgRepo(remoteRepoBaseUrl,getRepoName(RepoType.JAVA_DIST,repos),repoUser,repoPwd);

        set(RepoType.LOCAL, localRepo);
        set(RepoType.ZIP, zipRepo);
        set(RepoType.RPM,rpmRepo);
        set(RepoType.MAVEN,mavenRepo);
        set(RepoType.MAVEN_SNAPSHOT,mavenSnapshotRepo);
        set(RepoType.MAVEN_RELEASE,mavenReleaseRepo);
        set(RepoType.JAVA_DIST,javaDistRepo);
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

    private Resource getRepoNameResource() {
        String gradleHome = project.getGradle().getGradleUserHomeDir().getAbsolutePath();
        FileSystemResourceLoader loader = new FileSystemResourceLoader();
        String repoNamesJsonFilePath = "file://" + gradleHome + File.separator + REPO_NAMES_JSON_FILENAME;
        Resource repoNamesJsonAsResource = loader.getResource(repoNamesJsonFilePath);
        Assert.isTrue(repoNamesJsonAsResource.exists(), "repoNames.json file not found! repoNamesJsonFilePath = " + repoNamesJsonFilePath);
        return repoNamesJsonAsResource;
    }

    private Map getRepoNameJsonAsMap() {
        try {
            JsonSlurper slurper = new JsonSlurper();
            return (Map) slurper.parse(getRepoNameResource().getFile());
        } catch (IOException e) {
            throw new RuntimeException("common-repo Plugin couldn't be correctly instanciated, probably because " + REPO_NAMES_JSON_FILENAME + " couldn't be found. Original Exception Message was: " + e.getMessage());
        }
    }

    private String getRepoName(RepoType repo, List<Map> repos) {
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
