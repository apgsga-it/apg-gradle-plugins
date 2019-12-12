package com.apgsga.gradle.repo.util;

import com.apgsga.gradle.repo.extensions.RepoType;

import java.util.List;
import java.util.Map;

public class RepoNamesBean {

    //TODO JHE: Consider Lombok

    private List<Map<RepoType,String>> repos;
    private String repoUserName;
    private String repoUserPwd;
    private String repoBaseUrl;

    public String getRepoUserPwd() {
        return repoUserPwd;
    }

    public String getRepoUserName() {
        return repoUserName;
    }

    public String getRepoBaseUrl() {
        return repoBaseUrl;
    }

    public List<Map<RepoType, String>> getRepos() {
        return repos;
    }

}
