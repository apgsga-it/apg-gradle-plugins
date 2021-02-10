package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionPersistence
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RevisionManagerPatchImpl implements RevisionManager {

    private static Logger LOGGER = LoggerFactory.getLogger(RevisionManagerPatchImpl.class)

    private RevisionPersistence revisionPersistence

    RevisionManagerPatchImpl(RevisionPersistence revisionPersistence) {
        this.revisionPersistence = revisionPersistence
    }

    @Override
    String nextRevision() {
        def currentRev  = revisionPersistence.currentRevision() as Integer
        currentRev++
        revisionPersistence.save(currentRev as String)
        LOGGER.info("RevisionManagerPatchImpl: returning nextRevision: ${currentRev}")
        return currentRev
    }

    @Override
    void resetLastRevision(String serviceName, String target, String revision) {
        revisionPersistence.resetLastRevision(serviceName,target.toUpperCase(),revision.toUpperCase())
    }

    @Override
    String lastRevision(String serviceName, String target) {
        assert target != null , "Target must be set, cannot be null"
        String lastRevision = revisionPersistence.lastRevision(serviceName,target.toUpperCase())
        if(lastRevision != null) {
            LOGGER.info("RevisionManagerPatchImpl: returning lastRevision: ${lastRevision}")
            return lastRevision
        }
        else {
            LOGGER.info("RevisionManagerPatchImpl: returning lastRevision: SNAPSHOT, because lastRevision is null")
            return "SNAPSHOT"
        }
    }

    @Override
    void saveRevision(String serviceName, String target, String revision, String fullRevisionPrefix) {
        revisionPersistence.save(serviceName,target.toUpperCase(), revision.toUpperCase(), fullRevisionPrefix.toUpperCase())
    }


}