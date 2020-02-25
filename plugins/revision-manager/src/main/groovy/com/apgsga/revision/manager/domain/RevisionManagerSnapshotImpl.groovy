package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionPersistence

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