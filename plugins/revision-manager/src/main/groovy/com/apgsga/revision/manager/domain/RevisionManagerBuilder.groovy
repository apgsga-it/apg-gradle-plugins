package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionBeanBackedPersistence
import com.apgsga.revision.manager.persistence.RevisionPersistence
import com.apgsga.revision.manager.persistence.RevisionJsonFilePersistence


class RevisionManagerBuilder {

    // TODO (jhe, che , 21,2 ) : Tentative selection


    static enum Typ {
        PATCH, TEST_LOCAL
    }

    static Map<Typ,Class<? extends RevisionPersistence>> implemtationMap = new HashMap<Typ ,Class<? extends RevisionPersistence>>()

    static RevisionManagerBuilder create() {
        new RevisionManagerBuilder()
    }

    static {
        implemtationMap.put(Typ.PATCH, RevisionJsonFilePersistence.class)
        implemtationMap.put(Typ.TEST_LOCAL, RevisionBeanBackedPersistence.class)
    }

    private Typ typ = Typ.PATCH
    private String revisionRootPath;
    private String revisionFileName = "Revision.json"

    RevisionManager build() {
        validate(revisionRootPath != null, "Revision File Path may not be null")
        File revisionFileRoot = new File(revisionRootPath);
        validate(revisionFileRoot.exists(), "Parent Directory ${revisionRootPath} of ${revisionFileName} must exist")
        validate(revisionFileRoot.isDirectory(), "Parent Path ${revisionRootPath} of ${revisionFileName}  must be directory")
        def clx = implemtationMap.get(typ)
        println("Building Revision Manager with : ${this.toString()}")
        final def persistence = clx.newInstance(typ == Typ.PATCH ? new File(revisionRootPath, revisionFileName) : new File(revisionRootPath))
        new RevisionManagerImpl(persistence)
    }

    def revisionRootPath(def revisionRootPath) {
        this.revisionRootPath = revisionRootPath
        this
    }

    def revisionFileName(def revisionFileName) {
        this.revisionFileName = revisionFileName
        this
    }

    def typ(Typ typ) {
        this.typ = typ
        this
    }

    private validate(def valid , def message) {
        if (!valid) {
            throw new IllegalArgumentException(message)
        }
    }

    @Override
    public String toString() {
        return "RevisionManagerBuilder{" +
                "typ=" + typ +
                ", revisionRootPath='" + revisionRootPath + '\'' +
                ", revisionFileName='" + revisionFileName + '\'' +
                '}';
    }
}
