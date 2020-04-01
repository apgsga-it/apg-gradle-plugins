package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.Revisions
import com.apgsga.revision.manager.persistence.RevisionTargetHistory
import spock.lang.Ignore
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class RevisionManagerLocalPersistenceTests extends Specification {

    def static revisionRootPath = System.getProperty('java.io.tmpdir')
    def static revisionFilePath = "${revisionRootPath}/${Revisions.class.simpleName}.json"
    def static historyFilePath = "${revisionRootPath}/${RevisionTargetHistory.class.simpleName}.json"

    RevisionManager rm

    def setup() {
        rm = RevisionManagerBuilder.create()
                .revisionRootPath(revisionRootPath)
                .algorithm(RevisionManagerBuilder.AlgorithmTyp.PATCH)
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
            nextRev == "1"
    }

    def "Get next global Revision"() {
        when:
            def nextRev = rm.nextRevision()
        then:
            Files.exists(Paths.get(revisionFilePath))
            Files.exists(Paths.get(historyFilePath))
            nextRev == "1"
        when:
            RevisionManager rm2 =  RevisionManagerBuilder.create()
                    .revisionRootPath(revisionRootPath)
                    .algorithm(RevisionManagerBuilder.AlgorithmTyp.PATCH)
                    .build()
            nextRev = rm2.nextRevision()
        then:
            Files.exists(Paths.get(revisionFilePath))
            nextRev == "2"
    }

    def "Get last Revision for a service on a given target when the service is not yet in Revisions.json"() {
        given:
            Files.exists(Paths.get(revisionFilePath))
            Files.exists(Paths.get(historyFilePath))
        when:
            def lastRevForCHXXX = rm.lastRevision("myTestService","CHXXX")
        then:
            lastRevForCHXXX == "SNAPSHOT"
    }

    def "Save and get revisions for a service on given targets"() {
        when:
            rm.saveRevision("myTestService","chxxx", "22", "TEST-")
        then:
            Files.exists(Paths.get(revisionFilePath))
            Files.exists(Paths.get(historyFilePath))
        when:
            def lastRevForCHXXX = rm.lastRevision("myTestService","chxxx")
            def lastRevForCHYYY = rm.lastRevision("myTestService","chyyy")
        then:
            lastRevForCHXXX == "22"
            lastRevForCHYYY == "SNAPSHOT"
        when:
            rm.saveRevision("myTestService","chyyy", "33", "TEST-")
            lastRevForCHXXX = rm.lastRevision("myTestService","chxxx")
            lastRevForCHYYY = rm.lastRevision("myTestService","chyyy")
        then:
            lastRevForCHXXX == "22"
            lastRevForCHYYY == "33"
    }
}