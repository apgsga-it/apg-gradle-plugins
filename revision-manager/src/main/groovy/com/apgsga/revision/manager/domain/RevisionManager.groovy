package com.apgsga.revision.manager.domain

interface RevisionManager {

    def addRevision(def target, def revision, def fullRevisionPrefix)
    def lastRevision(def target)
    def nextRevision()
    def resetRevisions(def source, def target)
    def getRevisions(def target)
    def deleteRevisionsForTarget(def target, def revisions)
    def deleteRevisions(def revisions)
}