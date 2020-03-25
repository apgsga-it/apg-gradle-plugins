package com.apgsga.gradle.repo.plugin;

import com.apgsga.gradle.repo.extensions.RepoType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class RepoNamesBean {

    public String repoUserName;

    public String repoUserPwd;

    public String repoBaseUrl;

    @JsonProperty
    public List<Map<RepoType,String>> repos;
}
