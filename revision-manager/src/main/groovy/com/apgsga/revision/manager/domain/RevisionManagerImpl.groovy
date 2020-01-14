package com.apgsga.revision.manager.domain

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.Project

import java.util.stream.Collectors

class RevisionManagerImpl implements RevisionManager {

    private Project project;

    private revisionFile

    def RevisionManagerImpl(def project) {
        // TODO JHE: Instead of getting a config, we now get a project, and will read the config from there, assuming properties are in gradle.properties.
        //           This will certainly be done differently when IT-35189 will be done.
        this.project = project
        initRevisionFile()
    }

    @Override
    def nextRevision() {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        println revFileAsJson.nextRev
        incrementNextRev()
    }

    @Override
    def lastRevision(def target) {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        if(revFileAsJson."${target}" != null)
            println revFileAsJson."${target}".lastRevision
        else {
            println "SNAPSHOT"
        }
    }

    @Override
    def resetRevisions(def source, def target) {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        if(revFileAsJson."${target}" != null) {
            if(revFileAsJson."${source}" != null) {
                revFileAsJson."${target}".revisions = []
                revFileAsJson."${target}".lastRevision = revFileAsJson."${source}".lastRevision
                revisionFile.write(new JsonBuilder(revFileAsJson).toPrettyString())
            }
            else {
                revFileAsJson.remove(target)
                revisionFile.write(new JsonBuilder(revFileAsJson).toPrettyString())
            }
        }
    }

    @Override
    def getRevisions(def target) {
        def revisionsList = []
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        if(revFileAsJson."${target}" != null) {
            revisionsList = revFileAsJson."${target}".revisions.stream().collect(Collectors.toList())
        }
        if(revisionsList.isEmpty()) {
            print ""
        }
        else {
            println revisionsList.join(",")
        }
    }

    @Override
    def deleteRevisions(def revisions) {
        def revisionsAsList = revisions.trim().split(";")
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        def revisionsToBeDeletedForTarget = [:]
        revFileAsJson.each {targetInfo ->
            String key = targetInfo.key
            // Because of the structure of Revivions.json, we need to explicitely exclude "nextRev"
            if(!isProd(key) && !key.equalsIgnoreCase("nextRev")) {
                revisionsToBeDeletedForTarget.put(key, [])
                targetInfo.value.revisions.each { rev ->
                    def revNumber = rev.substring(rev.lastIndexOf('-')+1,rev.length())
                    if(revisionsAsList.contains(revNumber)) {
                        revisionsToBeDeletedForTarget.get(key).add(revNumber)
                    }
                }
            }
        }

        revisionsToBeDeletedForTarget.keySet().each { target ->
            deleteRevisionsForTarget(target, revisionsToBeDeletedForTarget.get(target).join(";"))
        }
    }

    @Override
    def deleteRevisionsForTarget(def target, def revisionsToBeDeleted) {
        assert !isProd(target) : "Revisions can't be deleted for production target: ${target}"
        def updatedRevisions = []
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        def revisionsToBeDeletedAsList = revisionsToBeDeleted.split(";")
        if(revFileAsJson."${target}" != null) {
            revFileAsJson."${target}".revisions.each { revision ->
                def revisionNumberOnly = revision.substring(revision.lastIndexOf("-")+1,revision.length())
                if(!(revisionsToBeDeletedAsList.contains(revisionNumberOnly))) {
                    updatedRevisions.add(revision)
                }
            }
            revFileAsJson."${target}".revisions = updatedRevisions
            revisionFile.write(new JsonBuilder(revFileAsJson).toPrettyString())
        }
    }

    @Override
    def addRevision(def target, def revision, def fullRevisionPrefix) {
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

    private def getInstalledRevisions(def target) {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        if(revFileAsJson."${target}" != null) {
            return revFileAsJson."${target}".revisions
        }
        else {
            return null
        }

    }

    private def incrementNextRev() {
        def revFileAsJson = new JsonSlurper().parse(revisionFile)
        def currentRev = revFileAsJson.nextRev
        currentRev++
        revFileAsJson.nextRev = currentRev
        revisionFile.write(new JsonBuilder(revFileAsJson).toPrettyString())
    }

    private def initRevisionFile() {
        revisionFile = new File(project.getProperties().get("revision.file.path"))
        if(!revisionFile.exists()) {
            def builder = new JsonBuilder()
            builder {
                nextRev(1)
            }
            revisionFile.write(builder.toPrettyString())
        }
    }

    private def isProd(def target) {
        def isProd = false
        def targetSystemMappingFilePath = "${project.getProperties().get('config.dir')}/${project.getProperties().get('target.system.mapping.file.name')}"
        def targetSystemMappingFile = new File(targetSystemMappingFilePath)
        assert targetSystemMappingFile.exists() : "${project.getProperties().get('config.dir')}/${project.getProperties().get('target.system.mapping.file.name')} does not exist!"
        def targetSystemMappingAsJson = new JsonSlurper().parse(targetSystemMappingFile)
        targetSystemMappingAsJson.stageMappings.each{stageMapping ->
            if(stageMapping.target.equalsIgnoreCase(target)) {
                isProd = stageMapping.name.equalsIgnoreCase("produktion")
                return // exit closure
            }
        }
        return isProd
    }
}