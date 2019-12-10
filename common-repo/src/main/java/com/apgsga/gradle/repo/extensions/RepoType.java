package com.apgsga.gradle.repo.extensions;

public enum RepoType {

    ZIP("ZIP"),
    RPM("RPM"),
    MAVEN("MAVEN"),
    MAVEN_SNAPSHOT("MAVEN_SNAPSHOT"),
    MAVEN_RELEASE("MAVEN_RELEASE"),
    LOCAL("LOCAL"),
    JAVA_DIST("JAVA_DIST");

    String name;

    RepoType(String name) {
        this.name = name;
    }

    public String asString() {
        return name;
    }
}
