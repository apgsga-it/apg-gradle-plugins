package com.apgsga.revision.manager.persistence


interface RevisionPersistence {

    /**
     * Retrieves last used global Revision Number
     * @return String : Revision Number as String
     */
    String currentRevision()

    /**
     * Retrieves last Revision Number of a Target System
     * @return String : Revision Number as String , may be null
     */
    String lastRevision(String serviceName, String target)

    /**
     * Saves Revision Number as current Global Revision Number
     */
    void save(String revision)

    /**
     * Historizes Version and Revision Number for a Target System
     */
    void save(String serviceName, String target, String revision, String versionPrefix)

    /**
     * Copy Revisions.json into the targetFolderPath
     * @param targetFolderPath
     */
    void cloneRevisionsJson(String targetFolderPath)

}
