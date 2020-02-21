package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.Revisions
import com.apgsga.revision.manager.persistence.TargetHistory
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class RevisionManagerLocalPersistenceTests extends Specification {

    def static revisionRootPath = System.getProperty('java.io.tmpdir')
    def static revisionFilePath = "${revisionRootPath}/${Revisions.class.simpleName}.json"
    def static historyFilePath = "${revisionRootPath}/${TargetHistory.class.simpleName}.json"

    RevisionManager rm

    def setup() {
        rm = RevisionManagerBuilder.create()
                .revisionRootPath(revisionRootPath)
                .typ(RevisionManagerBuilder.Typ.TEST_LOCAL)
                .build()
    }

    def cleanup() {
       Files.delete(Paths.get(revisionFilePath))
        Files.delete(Paths.get(historyFilePath))
    }

    def "Get next global Revision for first time"() {
        when:
        def nextRev = rm.nextRevision()
        then:
        Files.exists(Paths.get(revisionFilePath))
        Files.exists(Paths.get(historyFilePath))
        nextRev == 1
    }

    def "Get next global Revision"() {
        when:
        def nextRev = rm.nextRevision()
        then:
        Files.exists(Paths.get(revisionFilePath))
        Files.exists(Paths.get(historyFilePath))
        nextRev == 1
        when:
        RevisionManager rm2 =  RevisionManagerBuilder.create()
                .revisionRootPath(revisionRootPath)
                .typ(RevisionManagerBuilder.Typ.TEST_LOCAL)
                .build()
        nextRev = rm2.nextRevision()
        then:
        Files.exists(Paths.get(revisionFilePath))
        nextRev == 2
    }

    def "Get last Revision for a target when target not in Revisions.json"() {
        given:
        Files.exists(Paths.get(revisionFilePath))
        Files.exists(Paths.get(historyFilePath))
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
        Files.exists(Paths.get(historyFilePath))
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
