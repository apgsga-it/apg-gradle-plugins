package com.apgsga.revision.manager.persistence

import com.fasterxml.jackson.databind.ObjectMapper

class RevisionBeanBackedPersistence implements RevisionPersistence {

    File revisionRootDir

    RevisionBeanBackedPersistence(File revisionRootDir) {
        this.revisionRootDir = revisionRootDir
        println "Initializing ${this.toString()} with $revisionRootDir"
        init(Revisions.class, 0, new HashMap<String, String>())
        init(RevisionTargetHistory.class, new HashMap<String, List<String>>())
    }

    @Override
    def currentRevision() {
        read(Revisions.class).currentRevision
    }

    @Override
    def lastRevision(def targetName) {
        def revisions = read(Revisions.class)
        revisions.targets[targetName as String]
    }

    @Override
    def save(def revision) {
        def revisions = read(Revisions.class)
        revisions.currentRevision = revision
        write(revisions)
    }

    @Override
    def save(def target, def revision, def versionPrefix) {
        saveRevision(target, revision)
        saveRevisionHistory(target, versionPrefix, revision)
    }

    private void saveRevisionHistory(String target, versionPrefix, revision) {
        def targetsHistory = read(RevisionTargetHistory.class)
        targetsHistory.add(target,versionPrefix,revision)
        write(targetsHistory)
    }

    private void saveRevision(String targetName, revision) {
        def revisions = read(Revisions.class)
        revisions.targets[targetName as String] = revision
        write(revisions)
    }

    private  <T> T read(Class<T> clx) {
        String fileName = "${clx.simpleName}.json"
        File file = new File(revisionRootDir, fileName)
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(file, clx)
    }

    private <T> boolean exits(Class<T> clx) {
        String fileName = "${clx.simpleName}.json"
        File file = new File(revisionRootDir, fileName)
        file.exists()
    }

    private write(Object value) {
        String fileName = "${value.class.simpleName}.json"
        File file = new File(revisionRootDir, fileName)
        ObjectMapper mapper = new ObjectMapper()
        mapper.writeValue(file, value)
    }

    def <T> void init(Class<T> clx, Object[] constArgs) {
        println "Initializing ${this.toString()} with $constArgs"
        if (exits(clx)) return
        def obj = clx.newInstance(constArgs)
        println "About to write ${obj.toString()}"
        write(obj)
    }
}
