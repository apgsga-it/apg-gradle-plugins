package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionBeanBackedPersistence
import com.apgsga.revision.manager.persistence.RevisionPersistence
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class RevisionManagerBuilder {

    private static Logger LOGGER = LoggerFactory.getLogger(RevisionManagerBuilder.class)

    // TODO JHE: Shouldn't we deal with the history file as well ?
    private static String REVISION_FILENAME = "Revisions.json"

    static enum AlgorithmTyp {
        PATCH, SNAPSHOT, CLONED
    }

    static RevisionManagerBuilder create() {
        new RevisionManagerBuilder()
    }

    private AlgorithmTyp algorithmTyp = AlgorithmTyp.PATCH
    private String revisionRootPath
    private String cloneTargetPath

    RevisionManager build() {
        if (algorithmTyp == AlgorithmTyp.PATCH) {
            return buildPatchRevisionManager()
        } else if (algorithmTyp == AlgorithmTyp.CLONED) {
            return buildCloneRevisionManager()
        }
        else {
            return buildSnapshotRevisionManager()
        }
    }

    private RevisionManager buildCloneRevisionManager() {
        LOGGER.info("RevisionManagerBuilder: building a Clone Revision Manager with target: ${cloneTargetPath}")
        def revPersistence = getRevisionPersistence()
        revPersistence.cloneRevisionsJson(cloneTargetPath)
        revisionRootPath = cloneTargetPath
        new RevisionManagerClonedImpl(new RevisionManagerPatchImpl(revPersistence))
    }

    private RevisionManager buildPatchRevisionManager() {
        LOGGER.info("RevisionManagerBuilder: building a Patch Revision Manager with Revision Root path: ${revisionRootPath}")
        new RevisionManagerPatchImpl(getRevisionPersistence())
    }

    private RevisionPersistence getRevisionPersistence() {
        validate(revisionRootPath != null, "Revision File Path may not be null")
        File revisionFileRoot = new File(revisionRootPath)
        validate(revisionFileRoot.exists(), "Parent Directory ${revisionRootPath} of ${REVISION_FILENAME} must exist")
        validate(revisionFileRoot.isDirectory(), "Parent Path ${revisionRootPath} of ${REVISION_FILENAME}  must be directory")
        LOGGER.info("RevisionManagerBuilder: building Bean backed Persitistence Revision Manager with : ${this.toString()}")
        return new RevisionBeanBackedPersistence(revisionFileRoot)
    }

    private static RevisionManager buildSnapshotRevisionManager() {
        LOGGER.info("RevisionManagerBuilder: building Snapshot Revision Manager")
        new RevisionManagerSnapshotImpl()
    }

    RevisionManagerBuilder revisionRootPath(String revisionRootPath) {
        this.revisionRootPath = revisionRootPath
        this
    }

    RevisionManagerBuilder cloneTargetPath(String cloneTargetPath) {
        this.cloneTargetPath = cloneTargetPath
        this
    }

    RevisionManagerBuilder algorithm(AlgorithmTyp algorithmTyp) {
        this.algorithmTyp = algorithmTyp
        this
    }

    private static validate(def valid, def message) {
        if (!valid) {
            throw new IllegalArgumentException(message)
        }
    }

    @Override
    String toString() {
        return "RevisionManagerBuilder{" +
                ", algorithmTyp=" + algorithmTyp +
                ", revisionRootPath='" + revisionRootPath + '\'' +
                '}'
    }
}