package com.apgsga.gradle.ssh.zip.deployer.extensions;

import org.gradle.api.Project;

public class ApgZipDeployConfig {

    private final Project project;

    private String zipFilePath;
    private String zipFileName;
    private String remoteDeployDestFolder;
    private String remoteExtractDestFolder;
    private Boolean allowAnyHosts = false;

    public ApgZipDeployConfig(Project project) {
        this.project = project;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }

    public String getZipFilePath() {
        return zipFilePath;
    }

    public void setZipFilePath(String zipFilePath) {
        this.zipFilePath = zipFilePath;
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
                ", zipFilePath='" + zipFilePath + '\'' +
                ", zipFileName='" + zipFileName + '\'' +
                ", remoteDeployDestFolder='" + remoteDeployDestFolder + '\'' +
                ", remoteExtractDestFolder='" + remoteExtractDestFolder + '\'' +
                ", allowAnyHosts=" + allowAnyHosts +
                '}';
    }
}
