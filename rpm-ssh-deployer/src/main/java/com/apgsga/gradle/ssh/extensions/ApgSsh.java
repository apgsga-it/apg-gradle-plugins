package com.apgsga.gradle.ssh.extensions;

import org.gradle.api.Project;

public class ApgSsh {

    private final Project project;

    private String target;
    private String username;
    private String userpassword;
    private String rpmFilePath;
    private String rpmFileName;
    private String remoteDestFolder;

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

    public ApgSsh(Project project) {
        this.project = project;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public void log() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "ApgSsh{" +
                "target='" + target + '\'' +
                ", username='" + username + '\'' +
                ", rpmFilePath='" + rpmFilePath + '\'' +
                ", rpmFileName='" + rpmFileName + '\'' +
                ", remoteDestFolder='" + remoteDestFolder + '\'' +
                '}';
    }
}
