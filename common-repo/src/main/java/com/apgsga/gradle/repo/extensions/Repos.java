package com.apgsga.gradle.repo.extensions;

public interface Repos {

    Repo get(RepoType repoType);
    void set(RepoType repoType, Repo repo);
    Repo getArchiveFor(String archiveName);
}
