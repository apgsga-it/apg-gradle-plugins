package com.apgsga.gradle.repo.extensions;

public enum RepoType {

    ZIP("ZIP"),
    RPM("RPM"),
    MAVEN_SNAPSHOT("MAVEN_SNAPSHOT"),
    MAVEN_RELEASE("MAVEN_RELEASE");

    String name;

    RepoType(String name) {
        this.name = name;
    }

    public String asString() {
        return name;
    }
}
