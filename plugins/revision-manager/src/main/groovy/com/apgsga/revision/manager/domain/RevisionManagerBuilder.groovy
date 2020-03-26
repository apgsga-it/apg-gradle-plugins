package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionBeanBackedPersistence

class RevisionManagerBuilder {

    private static String REVISION_FILENAME = "Revisions.json"

    static enum AlgorithmTyp {
        PATCH, SNAPSHOT
    }

    static RevisionManagerBuilder create() {
        new RevisionManagerBuilder()
    }

    private AlgorithmTyp algorithmTyp = AlgorithmTyp.PATCH
    private String revisionRootPath

    RevisionManager build() {
        if (algorithmTyp == AlgorithmTyp.PATCH) {
            return buildPatchRevisionManager()
        } else {
            return buildSnapshotRevisionManager()
        }
    }

    private RevisionManager buildPatchRevisionManager() {
        validate(revisionRootPath != null, "Revision File Path may not be null")
        File revisionFileRoot = new File(revisionRootPath)
        validate(revisionFileRoot.exists(), "Parent Directory ${revisionRootPath} of ${REVISION_FILENAME} must exist")
        validate(revisionFileRoot.isDirectory(), "Parent Path ${revisionRootPath} of ${REVISION_FILENAME}  must be directory")
        println("Building Revision Manager with : ${this.toString()}")
        new RevisionManagerPatchImpl(new RevisionBeanBackedPersistence(revisionFileRoot))
    }
    private static RevisionManager buildSnapshotRevisionManager() {
        println("Building Snapshot Revision Manager")
        new RevisionManagerSnapshotImpl()
    }

    RevisionManagerBuilder revisionRootPath(String revisionRootPath) {
        this.revisionRootPath = revisionRootPath
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