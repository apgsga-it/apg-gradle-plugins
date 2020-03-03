package com.apgsga.revision.manager.persistence

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class RevisionJsonFilePersistence implements RevisionPersistence {

    File revisionFile

    RevisionJsonFilePersistence(File revisionFile) {
        this.revisionFile = revisionFile
        init()
    }

    @Override
    def currentRevision() {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        return revFileAsJson.nextRev
    }

    @Override
    def lastRevision(def target) {
        target = target.toUpperCase()
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        if (revFileAsJson."${target}" != null) {
            return revFileAsJson."${target}".lastRevision
        } else {
            return null
        }
    }

    @Override
    def save(def revision) {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        revFileAsJson.nextRev = revision
        revisionFile.write(new JsonBuilder(revFileAsJson).toPrettyString())
    }

    @Override
    def save(def target, def revision, def versionPrefix) {
        target = target.toUpperCase()
        revision = revision.toUpperCase()
        versionPrefix = versionPrefix.toUpperCase()
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        if (revFileAsJson."${target}" == null) {
            def builder = new JsonBuilder(revFileAsJson)
            builder {
                "${target}" {
                    lastRevision(revision)
                    revisions(["${versionPrefix}${revision}"])
                }
            }
            addNewContentToExistingRevisionFile(builder)
        } else {
            revFileAsJson."${target}".revisions.add("${versionPrefix}${revision}")
            revFileAsJson."${target}".lastRevision = revision
            revisionFile.write(new JsonBuilder(revFileAsJson).toPrettyString())
        }
    }

    private def addNewContentToExistingRevisionFile(JsonBuilder builder) {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        revisionFile.write(JsonOutput.prettyPrint(JsonOutput.toJson(revFileAsJson + builder.content)))
    }


    private void init() {
        if (revisionFile.exists()) {
            return
        }
        def builder = new JsonBuilder()
        builder {
            nextRev(0)
        }
        revisionFile.write(builder.toPrettyString())
    }
}
