package com.apgsga.gradle.repo.extensions;

public enum RepoProperties {
    REPO_BASE_URL("REPO_BASE_URL"),
    REPO_NAME("REPO_NAME"),
    REPO_PASSWORD("REPO_PASSWORD"),
    REPO_USER("REPO_USER");

    String name;

    RepoProperties(String name) {
        this.name = name;
    }

    public String asString() {
        return name;
    }
}
