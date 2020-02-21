package com.apgsga.revision.manager.domain


import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class RevisionManagerPatchPersistenceTests extends Specification {

    def static revisionRootPath = System.getProperty('java.io.tmpdir')
    def static revisionFileName = "Revisions.json"
    def static revisionFilePath = "${revisionRootPath}/${revisionFileName}"

    RevisionManager rm

    def setup() {
        rm = RevisionManagerBuilder.create()
                .revisionRootPath(revisionRootPath)
                .revisionFileName(revisionFileName)
                .typ(RevisionManagerBuilder.Typ.PATCH)
                .build()
    }

    def cleanup() {
       Files.delete(Paths.get(revisionFilePath))
    }

    def "Get next global Revision for first time"() {
        when:
        def nextRev = rm.nextRevision()
        then:
        Files.exists(Paths.get(revisionFilePath))
        nextRev == 1
    }

    def "Get next global Revision"() {
        when:
        def nextRev = rm.nextRevision()
        then:
        Files.exists(Paths.get(revisionFilePath))
        nextRev == 1
        when:
        RevisionManager rm2 =  RevisionManagerBuilder.create()
                .revisionRootPath(revisionRootPath)
                .revisionFileName(revisionFileName)
                .typ(RevisionManagerBuilder.Typ.PATCH)
                .build()
        nextRev = rm2.nextRevision()
        then:
        Files.exists(Paths.get(revisionFilePath))
        nextRev == 2
    }

    def "Get last Revision for a target when target not in Revisions.json"() {
        given:
        Files.exists(Paths.get(revisionFilePath))
        when:
        def lastRevForCHXXX = rm.lastRevision("CHXXX")
        then:
        lastRevForCHXXX == "SNAPSHOT"
    }

    def "Save and get revisions for targets"() {
        when:
        rm.saveRevision("chxxx", "22", "TEST-")
        then:
        Files.exists(Paths.get(revisionFilePath))
        when:
        def lastRevForCHXXX = rm.lastRevision("chxxx")
        def lastRevForCHYYY = rm.lastRevision("chyyy")
        then:
        lastRevForCHXXX == "22"
        lastRevForCHYYY == "SNAPSHOT"
        when:
        rm.saveRevision("chyyy", "33", "TEST-")
        lastRevForCHXXX = rm.lastRevision("chxxx")
        lastRevForCHYYY = rm.lastRevision("chyyy")
        then:
        lastRevForCHXXX == "22"
        lastRevForCHYYY == "33"
    }

}
