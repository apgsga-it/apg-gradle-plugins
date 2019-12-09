package com.apgsga.gradle.repo.extensions;

public interface Repos {

    Repo get(RepoType repoType);
    void set(RepoType repoType, Repo repo);

    //TODO JHE: I don't get/remember the purpose of this one. Do we really expect a Repo back? Not a File?
    Repo getArchiveFor(String archiveName);
}
