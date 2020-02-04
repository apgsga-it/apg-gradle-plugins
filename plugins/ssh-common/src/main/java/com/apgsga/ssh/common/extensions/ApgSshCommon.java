package com.apgsga.ssh.common.extensions;

import org.gradle.api.Project;

public class ApgSshCommon {

    private final Project project;

    // TODO JHE: consider having default values? Or loading default from a json config file?
    private String username;
    private String userpwd;
    private String destinationHost;

    public ApgSshCommon(Project project) {
        this.project = project;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getDestinationHost() {
        return destinationHost;
    }

    public void setDestinationHost(String destinationHost) {
        this.destinationHost = destinationHost;
    }

    public void log() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "ApgSshCommon{" +
                ", username='" + username + '\'' +
                ", userpwd='xxxx'" + '\'' +
                ", destinationHost='" + destinationHost + '\'' +
                '}';
    }
}
