package com.apgsga.revision.manager.domain

import com.apgsga.revision.manager.persistence.RevisionTargetHistory
import com.apgsga.revision.manager.persistence.Revisions
import spock.lang.Specification
import sun.security.x509.OtherName

import java.nio.file.Files
import java.nio.file.Paths

class RevisionManagerLocalPersistenceTests extends Specification {

    def static revisionsFileName = "${Revisions.class.simpleName}.json"
    def static revisionsHistoryFileName = "${RevisionTargetHistory.class.simpleName}.json"
    def static revisionRootPath = System.getProperty('java.io.tmpdir')
    def static revisionClonedPath = "${revisionRootPath}/revisionManagerClonedTest"
    def static revisionFilePath = "${revisionRootPath}/${revisionsFileName}"
    def static historyFilePath = "${revisionRootPath}/${revisionsHistoryFileName}"
    def static revisionClonedFilePath = "${revisionClonedPath}/${revisionsFileName}"

    RevisionManager revisionManagerPatch

    def setup() {
        revisionManagerPatch = RevisionManagerBuilder.create()
                .revisionRootPath(revisionRootPath)
                .algorithm(RevisionManagerBuilder.AlgorithmTyp.PATCH)
                .build()
    }

    def cleanup() {
        Files.delete(Paths.get(revisionFilePath))
        Files.delete(Paths.get(historyFilePath))
        new File(revisionClonedPath).deleteDir()
    }

    def "Get next global Revision for first time"() {
        when:
            def nextRev = revisionManagerPatch.nextRevision()
        then:
            Files.exists(Paths.get(revisionFilePath))
            Files.exists(Paths.get(historyFilePath))
            nextRev == "1"
    }

    def "Get next global Revision"() {
        when:
            def nextRev = revisionManagerPatch.nextRevision()
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
            def lastRevForCHXXX = revisionManagerPatch.lastRevision("myTestService","CHXXX")
        then:
            lastRevForCHXXX == "SNAPSHOT"
    }

    def "Save and get revisions for a service on given targets"() {
        when:
            revisionManagerPatch.saveRevision("myTestService","chxxx", "22", "TEST-")
        then:
            Files.exists(Paths.get(revisionFilePath))
            Files.exists(Paths.get(historyFilePath))
        when:
            def lastRevForCHXXX = revisionManagerPatch.lastRevision("myTestService","chxxx")
            def lastRevForCHYYY = revisionManagerPatch.lastRevision("myTestService","chyyy")
        then:
            lastRevForCHXXX == "22"
            lastRevForCHYYY == "SNAPSHOT"
        when:
            revisionManagerPatch.saveRevision("myTestService","chyyy", "33", "TEST-")
            lastRevForCHXXX = revisionManagerPatch.lastRevision("myTestService","chxxx")
            lastRevForCHYYY = revisionManagerPatch.lastRevision("myTestService","chyyy")
        then:
            lastRevForCHXXX == "22"
            lastRevForCHYYY == "33"
    }

    def "Ensure RevisionManager for clone scenario is throwing exception"() {
        when:
            new File(revisionClonedPath).mkdirs()
            def revisionManagerCloned = RevisionManagerBuilder.create()
                    .revisionRootPath(revisionClonedPath)
                    .algorithm(RevisionManagerBuilder.AlgorithmTyp.CLONED)
                    .build()
            revisionManagerCloned.clone("dummy")
        then:
            thrown RuntimeException
        when:
            revisionManagerCloned.nextRevision()
        then:
            thrown RuntimeException
        when:
            revisionManagerCloned.saveRevision("dummyServiceName","dummyTarget","dummyRevision","dummyFullRevisionPrefix")
        then:
            thrown RuntimeException
    }

    def "Ensure cloning and getting information from clone is working"() {
        // TODO JHE
        /*
        when:
            revisionManagerPatch.saveRevision("testService_1","chei212","10","TEST-")
        then:
            Files.exists(Paths.get(revisionFilePath))
            Files.exists(Paths.get(historyFilePath))
            !Files.exists(Paths.get(revisionClonedFilePath))
        when:
            new File(revisionClonedPath).mkdirs()
            revisionManagerPatch.clone(revisionClonedPath)
        then:
            Files.exists(Paths.get(revisionFilePath))
            Files.exists(Paths.get(historyFilePath))
            Files.exists(Paths.get(revisionClonedFilePath))

         */
    }
}