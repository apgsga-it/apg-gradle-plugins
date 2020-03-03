package com.apgsga.revision.manager.domain

class RevisionManagerSnapshotImpl implements RevisionManager {


    @Override
    def nextRevision() {
       "SNAPSHOT"
    }

    @Override
    def lastRevision(def target) {
        "SNAPSHOT"
    }

    @Override
    void saveRevision(def target, def revision, def fullRevisionPrefix) {
    }


}