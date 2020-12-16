package com.apgsga.revision.manager.domain
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RevisionManagerSnapshotImpl implements RevisionManager {

    private static Logger LOGGER = LoggerFactory.getLogger(RevisionManagerBuilder.class)


    @Override
    String nextRevision() {
        LOGGER.info("RevisionManagerSnapshotImpl: Returning nextRevision: SNAPSHOT")
       "SNAPSHOT"
    }

    @Override
    String lastRevision(String serviceName, String target) {
        LOGGER.info("RevisionManagerSnapshotImpl: Last Revision for ${serviceName} and ${target}: SNAPSHOT")
        "SNAPSHOT"
    }

    @Override
    void saveRevision(String serviceName, String target, String revision, String fullRevisionPrefix) {
        LOGGER.info("RevisionManagerSnapshotImpl: Doing nothing on saveRevision for ${serviceName} and ${target}")
    }
}