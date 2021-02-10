package com.apgsga.revision.manager.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RevisionBeanBackedPersistence implements RevisionPersistence {

    private static Logger LOGGER = LoggerFactory.getLogger(RevisionBeanBackedPersistence.class)

    File revisionRootDir

    RevisionBeanBackedPersistence(File revisionRootDir) {
        this.revisionRootDir = revisionRootDir
        init(Revisions.class, "0", new HashMap<String,HashMap<String, String>>())
        init(RevisionTargetHistory.class, new HashMap<String,HashMap<String, List<String>>>())
    }

    @Override
    String currentRevision() {
        def cr = read(Revisions.class).currentRevision
        println "RevisionBeanBackedPersistence : current Revision: ${cr}"
        return cr
    }

    @Override
    String lastRevision(String serviceName, String targetName) {
        LOGGER.info "RevisionBeanBackedPersistence: getting lastRevision for service: ${serviceName} and ${targetName}"
        def revisions = read(Revisions.class)
        if(revisions.services.get(serviceName) != null) {
            def lr = revisions.services.get(serviceName).get(targetName)
            LOGGER.info "RevisionBeanBackedPersistence: got lastRevision: ${lr} for service: ${serviceName} and ${targetName}"
            return lr
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

    @Override
    void cloneRevisionsJson(String targetFolderPath) {
        def revisions = read(Revisions.class)
        write(revisions,new File(targetFolderPath))
    }

    @Override
    void resetLastRevision(String serviceName, String target, String revision) {
        saveRevision(serviceName,target,revision)
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

    private static write(Object value, File rootDir) {
        String fileName = "${value.class.simpleName}.json"
        File file = new File(rootDir as File, fileName)
        ObjectMapper mapper = new ObjectMapper()
        mapper.writeValue(file, value)
    }

    private write(Object value) {
        write(value,revisionRootDir)
    }

    def <T> void init(Class<T> clx, Object[] constArgs) {
        if (exists(clx)) return
        def obj = clx.newInstance(constArgs)
        write(obj)
    }

    @Override
    String toString() {
        return "RevisionBeanBackedPersistence{" +
                ", revisionRootDir=" + revisionRootDir +
                '}'
    }

}
