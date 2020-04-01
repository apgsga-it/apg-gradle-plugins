package com.apgsga.revision.manager.persistence

import com.fasterxml.jackson.databind.ObjectMapper

class RevisionBeanBackedPersistence implements RevisionPersistence {

    File revisionRootDir

    RevisionBeanBackedPersistence(File revisionRootDir) {
        this.revisionRootDir = revisionRootDir
        init(Revisions.class, "0", new HashMap<String,HashMap<String, String>>())
        init(RevisionTargetHistory.class, new HashMap<String,HashMap<String, List<String>>>())
    }

    @Override
    String currentRevision() {
        read(Revisions.class).currentRevision
    }

    @Override
    String lastRevision(String serviceName, String targetName) {
        def revisions = read(Revisions.class)
        if(revisions.services.get(serviceName) != null) {
            return revisions.services.get(serviceName).get(targetName)
        }
        return null
    }

    @Override
    void save(String revision) {
        def revisions = read(Revisions.class)
        revisions.currentRevision = revision
        write(revisions)
    }

    @Override
    void save(String serviceName, String target, String revision, String versionPrefix) {
        saveRevision(serviceName,target, revision)
        saveRevisionHistory(serviceName,target, versionPrefix, revision)
    }

    private void saveRevisionHistory(String serviceName, String target, String versionPrefix, String revision) {
        def targetsHistory = read(RevisionTargetHistory.class)
        targetsHistory.add(serviceName,target,versionPrefix,revision)
        write(targetsHistory)
    }

    private void saveRevision(String serviceName, String targetName, String revision) {
        def revisions = read(Revisions.class)
        if(!revisions.services.keySet().contains(serviceName)) {
            revisions.services.put(serviceName, new HashMap<String, String>())
        }
        revisions.services.get(serviceName).put(targetName,revision)
        write(revisions)
    }

    private  <T> T read(Class<T> clx) {
        String fileName = "${clx.simpleName}.json"
        File file = new File(revisionRootDir as File, fileName)
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(file, clx)
    }

    private <T> boolean exists(Class<T> clx) {
        String fileName = "${clx.simpleName}.json"
        File file = new File(revisionRootDir as File, fileName)
        file.exists()
    }

    private write(Object value) {
        String fileName = "${value.class.simpleName}.json"
        File file = new File(revisionRootDir as File, fileName)
        ObjectMapper mapper = new ObjectMapper()
        mapper.writeValue(file, value)
    }

    def <T> void init(Class<T> clx, Object[] constArgs) {
        if (exists(clx)) return
        def obj = clx.newInstance(constArgs)
        write(obj)
    }
}
