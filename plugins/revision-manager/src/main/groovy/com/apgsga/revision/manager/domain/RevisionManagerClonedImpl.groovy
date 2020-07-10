package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionPersistence

class RevisionManagerClonedImpl implements RevisionManager{

    private RevisionPersistence revisionPersistence

    RevisionManagerClonedImpl(RevisionPersistence revisionPersistence) {
        this.revisionPersistence = revisionPersistence
    }

    @Override
    void saveRevision(String serviceName, String target, String revision, String fullRevisionPrefix) {
        throw new RuntimeException("saveRevision not supported by ${RevisionManagerClonedImpl.class.name}")
    }

    @Override
    String lastRevision(String serviceName, String target) {
        assert target != null , "Target must be set, cannot be null"
        String lastRevision = revisionPersistence.lastRevision(serviceName,target.toUpperCase())
        if(lastRevision != null)
            return lastRevision
        else {
            return "SNAPSHOT"
        }
    }

    @Override
    String nextRevision() {
        throw new RuntimeException("nextRevision not supported by ${RevisionManagerClonedImpl.class.name}")
    }
}
