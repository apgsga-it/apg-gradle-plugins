package com.apgsga.ssh.extensions;

import org.gradle.api.Project;

public class ApgZipDeployConfig {

    private final Project project;

    private String zipFileParentPath;
    private String remoteDeployDestFolder;
    private String remoteExtractDestFolder;
    private Boolean allowAnyHosts = false;

    public ApgZipDeployConfig(Project project) {
        this.project = project;
    }

    public String getZipFileParentPath() {
        return zipFileParentPath;
    }

    public void setZipFileParentPath(String zipFileParentPath) {
        this.zipFileParentPath = zipFileParentPath;
    }

    public String getRemoteDeployDestFolder() {
        return remoteDeployDestFolder;
    }

    public void setRemoteDeployDestFolder(String remoteDeployDestFolder) { this.remoteDeployDestFolder = remoteDeployDestFolder; }

    public String getRemoteExtractDestFolder() { return remoteExtractDestFolder; }

    public void setRemoteExtractDestFolder(String remoteExtractDestFolder) { this.remoteExtractDestFolder = remoteExtractDestFolder; }

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
        return "ApgZipDeployConfig{" +
                "project=" + project +
                ", zipFileParentPath='" + zipFileParentPath + '\'' +
                ", remoteDeployDestFolder='" + remoteDeployDestFolder + '\'' +
                ", remoteExtractDestFolder='" + remoteExtractDestFolder + '\'' +
                ", allowAnyHosts=" + allowAnyHosts +
                '}';
    }
}
