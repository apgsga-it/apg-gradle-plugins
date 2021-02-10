package com.apgsga.revision.manager.domain

interface RevisionManager {

    /**
     * @param target
     * @param revision
     * @param fullRevisionPrefix
     * @return void
     */
    void saveRevision(String serviceName, String target, String revision, String fullRevisionPrefix)

    /**
     *
     * @param target
     * @return last revision for the given target
     */
    String lastRevision(String serviceName, String target)

    /**
     * Fetch the next global revision and set lastRevForTarget and nextRevForTarget of NextRevision task
     * @return revision
     */
    String nextRevision()

    /**
     * Reset the last revision of a service for a given target
     * @param serviceName
     * @param target
     * @param revision
     */
    void resetLastRevision(String serviceName, String target, String revision)
}
