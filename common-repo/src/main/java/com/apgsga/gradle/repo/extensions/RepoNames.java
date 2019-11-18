package com.apgsga.gradle.repo.extensions;

public enum RepoNames {

    ZIP("ZIP"),
    RPM("RPM"),
    MAVEN_SNAPSHOT("MAVEN_SNAPSHOT"),
    MAVEN_RELEASE("MAVEN_RELEASE");

    String name;

    RepoNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
