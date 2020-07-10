package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionPersistence

class RevisionManagerPatchImpl implements RevisionManager {

    private RevisionPersistence revisionPersistence

    RevisionManagerPatchImpl(RevisionPersistence revisionPersistence) {
        this.revisionPersistence = revisionPersistence
    }

    @Override
    String nextRevision() {
        def currentRev =revisionPersistence.currentRevision()
        currentRev++
        revisionPersistence.save(currentRev)
        return currentRev
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
    void saveRevision(String serviceName, String target, String revision, String fullRevisionPrefix) {
        revisionPersistence.save(serviceName,target.toUpperCase(), revision.toUpperCase(), fullRevisionPrefix.toUpperCase())
    }


}