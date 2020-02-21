package com.apgsga.revision.manager.domain

// TODO (jhe, che , 21.2 ) The interface should have explicitly defined types
interface RevisionManager {

    /**
     * @param target
     * @param revision
     * @param fullRevisionPrefix
     * @return void
     */
    void saveRevision(def target, def revision, def fullRevisionPrefix)

    /**
     *
     * @param target
     * @return last revision for the given target
     */
    def lastRevision(def target)

    /**
     * Fetch the next global revision and set lastRevForTarget and nextRevForTarget of NextRevision task
     * @return revision
     */
    def nextRevision()
}