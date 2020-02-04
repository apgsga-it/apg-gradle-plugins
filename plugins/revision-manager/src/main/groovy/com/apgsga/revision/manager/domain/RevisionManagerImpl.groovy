package com.apgsga.revision.manager.domain


import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class RevisionManagerImpl implements RevisionManager {

    private Properties config

    private revisionFile

    RevisionManagerImpl(Properties configuration) {
        this.config = configuration
        initRevisionFile()
    }

    @Override
    def nextRevision() {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        def nextRev = revFileAsJson.nextRev
        incrementNextRev()
        return nextRev
    }

    @Override
    String lastRevision(def target) {
        target = target.toUpperCase()
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        if(revFileAsJson."${target}" != null)
            return revFileAsJson."${target}".lastRevision
        else {
            return "SNAPSHOT"
        }
    }

    @Override
    def saveRevision(def target, def revision, def fullRevisionPrefix) {
        target = target.toUpperCase()
        revision = revision.toUpperCase()
        fullRevisionPrefix = fullRevisionPrefix.toUpperCase()
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        if(revFileAsJson."${target}" == null) {
            def builder = new JsonBuilder(revFileAsJson)
            builder{
                "${target}"{
                    lastRevision(revision)
                    revisions(["${fullRevisionPrefix}${revision}"])
                }
            }
            addNewContentToExistingRevisionFile(builder)
        }
        else {
            revFileAsJson."${target}".revisions.add("${fullRevisionPrefix}${revision}")
            revFileAsJson."${target}".lastRevision = revision
            revisionFile.write(new JsonBuilder(revFileAsJson).toPrettyString())
        }
    }

    private def addNewContentToExistingRevisionFile(JsonBuilder builder) {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        revisionFile.write(JsonOutput.prettyPrint(JsonOutput.toJson(revFileAsJson + builder.content)))
    }

    private def incrementNextRev() {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        def currentRev = revFileAsJson.nextRev
        currentRev++
        revFileAsJson.nextRev = currentRev
        revisionFile.write(new JsonBuilder(revFileAsJson).toPrettyString())
    }

    private def initRevisionFile() {
        revisionFile = new File(config.get("revision.file.path"))
        if(!revisionFile.exists()) {
            def builder = new JsonBuilder()
            builder {
                nextRev(1)
            }
            revisionFile.write(builder.toPrettyString())
        }
    }
}