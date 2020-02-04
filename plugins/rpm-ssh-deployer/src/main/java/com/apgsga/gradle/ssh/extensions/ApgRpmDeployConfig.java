package com.apgsga.gradle.ssh.extensions;

import org.gradle.api.Project;

public class ApgRpmDeployConfig {

    private final Project project;

    private String rpmFilePath;
    private String rpmFileName;
    private String remoteDestFolder;
    private Boolean allowAnyHosts = false;

    public ApgRpmDeployConfig(Project project) {
        this.project = project;
    }

    public String getRpmFileName() {
        return rpmFileName;
    }

    public void setRpmFileName(String rpmFileName) {
        this.rpmFileName = rpmFileName;
    }

    public String getRpmFilePath() {
        return rpmFilePath;
    }

    public void setRpmFilePath(String rpmFilePath) {
        this.rpmFilePath = rpmFilePath;
    }

    public String getRemoteDestFolder() {
        return remoteDestFolder;
    }

    public void setRemoteDestFolder(String remoteDestFolder) {
        this.remoteDestFolder = remoteDestFolder;
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
        return "ApgRpmDeployConfig{" +
                "project=" + project +
                ", rpmFilePath='" + rpmFilePath + '\'' +
                ", rpmFileName='" + rpmFileName + '\'' +
                ", remoteDestFolder='" + remoteDestFolder + '\'' +
                ", allowAnyHosts=" + allowAnyHosts +
                '}';
    }
}
