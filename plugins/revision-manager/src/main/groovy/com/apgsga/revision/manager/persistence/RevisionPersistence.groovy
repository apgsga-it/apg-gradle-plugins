package com.apgsga.revision.manager.persistence

interface RevisionPersistence {

    /**
     * Retrieves last used global Revision Number
     * @return String : Revision Number as String
     */
    def currentRevision()

    /**
     * Retrieves last Revision Number of a Target System
     * @return String : Revision Number as String , may be null
     */
    def lastRevision(def target)

    /**
     * Saves Revision Number as current Global Revision Number
     */
    def save(def revision)

    /**
     * Historizes Version and Revision Number for a Target System
     */
    def save(def target, def revision, def versionPrefix)

}
