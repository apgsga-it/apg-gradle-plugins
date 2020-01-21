package com.apgsga.revision.manager.domain

interface RevisionManager {

    /**
     *
     * @param target
     * @param revision
     * @param fullRevisionPrefix
     * @return void
     */
    def saveRevision(def target, def revision, def fullRevisionPrefix)

    /**
     *
     * @param target
     * @return last revision for the given target
     */
    def lastRevision(def target)

    /**
     * Fetch the next global revision and set lastRevForTarget and nextRevForTarget of NextRevision task
     * @param : target, which is a Gradle Project properties
     * @return void
     */
    def nextRevision()
}