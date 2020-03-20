package com.apgsga.ssh.extensions;

import org.gradle.api.Project;

public class ApgSshConfiguration {

    private final Project project;

    // TODO JHE: consider having default values? Or loading default from a json config file?
    private String username;
    private String userpwd;
    private String destinationHost;
    private Boolean allowAnyHosts = false;

    public ApgSshConfiguration(Project project) {
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

    public Boolean getAllowAnyHosts() {
        return allowAnyHosts;
    }

    public void setAllowAnyHosts(Boolean allowAnyHosts) {
        this.allowAnyHosts = allowAnyHosts;
    }

    public void log() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "ApgSshConfiguration{" +
                "username='" + username + '\'' +
                ", userpwd='xxxx'" +
                ", destinationHost='" + destinationHost + '\'' +
                ", allowAnyHosts=" + allowAnyHosts +
                '}';
    }
}
