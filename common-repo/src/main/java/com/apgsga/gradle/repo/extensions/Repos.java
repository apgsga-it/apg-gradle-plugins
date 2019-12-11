package com.apgsga.gradle.repo.extensions;

public interface Repos {

    String COMMMON_REPO_PLUGIN_NAME = "apgRepos";

    Repo get(RepoType repoType);
    void set(RepoType repoType, Repo repo);
    Repo getRepoFor(String archiveName);
}
