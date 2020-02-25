package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionPersistence

class RevisionManagerPatchImpl implements RevisionManager {

    private RevisionPersistence revisionPersistence

    RevisionManagerPatchImpl(RevisionPersistence revisionPersistence) {
        this.revisionPersistence = revisionPersistence
    }

    @Override
    def nextRevision() {
        def currentRev =revisionPersistence.currentRevision()
        currentRev++
        revisionPersistence.save(currentRev)
        return currentRev
    }

    @Override
    def lastRevision(def target) {
        String lastRevision = revisionPersistence.lastRevision(target.toUpperCase())
        if(lastRevision != null)
            return lastRevision
        else {
            return "SNAPSHOT"
        }
    }

    @Override
    void saveRevision(def target, def revision, def fullRevisionPrefix) {
        revisionPersistence.save(target.toUpperCase(), revision.toUpperCase(), fullRevisionPrefix.toUpperCase())
    }


}