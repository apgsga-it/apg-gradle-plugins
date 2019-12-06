package com.apgsga.gradle.repo.extensions;

//TODO JHE: Not sure we want this interface, but in a way it serves as interface between build.gradle and our plugin ...
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
