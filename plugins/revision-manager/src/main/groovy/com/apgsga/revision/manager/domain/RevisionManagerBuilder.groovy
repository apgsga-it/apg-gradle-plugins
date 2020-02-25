package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionBeanBackedPersistence
import com.apgsga.revision.manager.persistence.RevisionPersistence
import com.apgsga.revision.manager.persistence.RevisionJsonFilePersistence


class RevisionManagerBuilder {

    // TODO (jhe, che , 21,2 ) : Tentative selection
    private static String REVISION_FILENAME = "Revisions.json"

    static enum PersistenceTyp {
        PATCH, TEST_LOCAL
    }

    static enum AlgorithmTyp {
        PATCH, SNAPSHOT
    }

    static Map<PersistenceTyp,Class<? extends RevisionPersistence>> persistenceImplMap = new HashMap<PersistenceTyp ,Class<? extends RevisionPersistence>>()
    static {
        persistenceImplMap.put(PersistenceTyp.PATCH, RevisionJsonFilePersistence.class)
        persistenceImplMap.put(PersistenceTyp.TEST_LOCAL, RevisionBeanBackedPersistence.class)
    }

    static RevisionManagerBuilder create() {
        new RevisionManagerBuilder()
    }


    private PersistenceTyp persistenceTyp = PersistenceTyp.PATCH
    private AlgorithmTyp algorithmTyp = AlgorithmTyp.PATCH
    private String revisionRootPath;

    RevisionManager build() {
        if (algorithmTyp == AlgorithmTyp.PATCH) {
            return buildPatchRevisionManager()
        } else {
            return buildSnapshotRevisionManager()
        }
    }

    private RevisionManager buildPatchRevisionManager() {
        validate(revisionRootPath != null, "Revision File Path may not be null")
        File revisionFileRoot = new File(revisionRootPath);
        validate(revisionFileRoot.exists(), "Parent Directory ${revisionRootPath} of ${REVISION_FILENAME} must exist")
        validate(revisionFileRoot.isDirectory(), "Parent Path ${revisionRootPath} of ${REVISION_FILENAME}  must be directory")
        def clx = persistenceImplMap.get(persistenceTyp)
        println("Building Revision Manager with : ${this.toString()}")
        final def persistence = clx.newInstance(persistenceTyp == PersistenceTyp.PATCH ? new File(revisionRootPath, REVISION_FILENAME) : new File(revisionRootPath))
        new RevisionManagerPatchImpl(persistence)
    }
    private RevisionManager buildSnapshotRevisionManager() {
        println("Building Snapshot Revision Manager")
        new RevisionManagerSnapshotImpl()
    }

    RevisionManagerBuilder revisionRootPath(String revisionRootPath) {
        this.revisionRootPath = revisionRootPath
        this
    }

    RevisionManagerBuilder persistence(PersistenceTyp persistenceTyp) {
        this.persistenceTyp = persistenceTyp
        this
    }

    RevisionManagerBuilder algorithm(AlgorithmTyp algorithmTyp) {
        this.algorithmTyp = algorithmTyp
        this
    }

    private validate(def valid , def message) {
        if (!valid) {
            throw new IllegalArgumentException(message)
        }
    }

    @Override
    String toString() {
        return "RevisionManagerBuilder{" +
                "persistenceTyp=" + persistenceTyp +
                ", algorithmTyp=" + algorithmTyp +
                ", revisionRootPath='" + revisionRootPath + '\'' +
                '}';
    }

}
