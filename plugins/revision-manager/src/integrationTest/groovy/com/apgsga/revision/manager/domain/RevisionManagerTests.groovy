package com.apgsga.revision.manager.domain


import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class RevisionManagerTests extends Specification {

    Properties props
    def revisionFilePath = System.getProperty('java.io.tmpdir') + "/Revisions.json"

    def setup(){
        props = new Properties()
        props.put("revision.file.path",revisionFilePath)
    }

    def "Get next global Revision for first time"() {
        when:
            RevisionManager rm = new RevisionManagerImpl(props)
            def nextRev = rm.nextRevision()
        then:
            Files.exists(Paths.get(revisionFilePath))
            nextRev == 1
        cleanup:
            Files.delete(Paths.get(revisionFilePath))
    }

    def "Get next global Revision"() {
        when:
            RevisionManager rm = new RevisionManagerImpl(props)
            def nextRev = rm.nextRevision()
        then:
            Files.exists(Paths.get(revisionFilePath))
            nextRev == 1
        when:
            RevisionManager rm2 = new RevisionManagerImpl(props)
            nextRev = rm2.nextRevision()
        then:
            Files.exists(Paths.get(revisionFilePath))
            nextRev == 2
        cleanup:
            Files.delete(Paths.get(revisionFilePath))
    }

    def "Get last Revision for a target when target not in Revisions.json"() {
        when:
            RevisionManager rm = new RevisionManagerImpl(props)
        then:
            Files.exists(Paths.get(revisionFilePath))
        when:
            def lastRevForCHXXX = rm.lastRevision("CHXXX")
        then:
            lastRevForCHXXX == "SNAPSHOT"
        cleanup:
            Files.delete(Paths.get(revisionFilePath))
    }

    def "Save and get revisions for targets"() {
        when:
            RevisionManager rm = new RevisionManagerImpl(props)
            rm.saveRevision("chxxx","22","TEST-")
        then:
            Files.exists(Paths.get(revisionFilePath))
        when:
            def lastRevForCHXXX = rm.lastRevision("chxxx")
            def lastRevForCHYYY = rm.lastRevision("chyyy")
        then:
            lastRevForCHXXX == "22"
            lastRevForCHYYY == "SNAPSHOT"
        when:
            rm.saveRevision("chyyy","33","TEST-")
            lastRevForCHXXX = rm.lastRevision("chxxx")
            lastRevForCHYYY = rm.lastRevision("chyyy")
        then:
            lastRevForCHXXX == "22"
            lastRevForCHYYY == "33"
        cleanup:
            Files.delete(Paths.get(revisionFilePath))
    }

}
