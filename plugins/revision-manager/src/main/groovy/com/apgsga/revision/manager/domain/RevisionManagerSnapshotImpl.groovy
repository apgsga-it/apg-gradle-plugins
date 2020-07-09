package com.apgsga.revision.manager.domain

class RevisionManagerSnapshotImpl implements RevisionManager {


    @Override
    String nextRevision() {
       "SNAPSHOT"
    }

    @Override
    void clone(String targetFolderAbsolutePath) {

    }

    @Override
    String lastRevision(String serviceName, String target) {
        "SNAPSHOT"
    }

    @Override
    void saveRevision(String serviceName, String target, String revision, String fullRevisionPrefix) {
    }


}