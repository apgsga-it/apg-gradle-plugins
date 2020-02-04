package com.apgsga.revision.manager

import com.apgsga.gradle.test.utils.AbstractSpecification
import groovy.json.JsonSlurper

class SaveRevisionTests extends AbstractSpecification {

    def "Validate save revision for target without any parameter"() {
        given:
            buildFile << """
                            plugins {
                                id 'com.apgsga.revision.manager' 
                            }
                        """
            def revFile = new File("src/functionalTest/resources/Revisions.json")
        when:
            def result = gradleRunnerFactory(['init', 'saveRevision']).build()
        then:
            // JHE: mmhh, a bit generic ;), but really important to test here is that we get an Exception
            thrown Exception
        cleanup:
            revFile.delete()
    }

    def "Validate save revision for target"() {
        given:
            buildFile << """
                        plugins {
                            id 'com.apgsga.revision.manager' 
                        }
                    """
            def revFile = new File("src/functionalTest/resources/Revisions.json")
        when:
            def result = gradleRunnerFactory(['init', 'saveRevision', '-Ptarget=chei212', '-Prevision=123', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            def chei212Revisions
            def prodRevision
            def lastChei212Revision
            def nextGlobalRevision
            def revAsJson = new JsonSlurper().parse(revFile)
        then:
            // result.returnCode == 0
            revAsJson.nextRev == 1 // because we started with a brand new Revision -> although it has no meaning for this particular test
            revAsJson.prodRevision == null // because we started with a brand new Revision -> although it has no meaning for this particular test
            revAsJson.CHEI212.revisions.size() == 1
            revAsJson.CHEI212.revisions[0].toString() == "9.1.0.ADMIN-UIMIG-123"
            revAsJson.CHEI212.lastRevision.toString() == "123"
        when:
            result = gradleRunnerFactory(['init', 'saveRevision', '-Ptarget=chei212', '-Prevision=234', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            revAsJson = new JsonSlurper().parse(revFile)
        then:
            revFile.exists()
            revAsJson.nextRev == 1 // because we started with a brand new Revision -> although it has no meaning for this particular test
            revAsJson.prodRevision == null // because we started with a brand new Revision -> although it has no meaning for this particular test
            revAsJson.CHEI212.revisions.size() == 2
            revAsJson.CHEI212.revisions.contains("9.1.0.ADMIN-UIMIG-123")
            revAsJson.CHEI212.revisions.contains("9.1.0.ADMIN-UIMIG-234")
            revAsJson.CHEI212.lastRevision.toString() == "234"
        when:
            result = gradleRunnerFactory(['init', 'saveRevision', '-Ptarget=chti211', '-Prevision=15', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            revAsJson = new JsonSlurper().parse(revFile)
        then:
            revFile.exists()
            revAsJson.nextRev == 1 // because we started with a brand new Revision -> although it has no meaning for this particular test
            revAsJson.prodRevision == null // because we started with a brand new Revision -> although it has no meaning for this particular test
            revAsJson.CHEI212.revisions.size() == 2
            revAsJson.CHEI212.revisions.contains("9.1.0.ADMIN-UIMIG-123")
            revAsJson.CHEI212.revisions.contains("9.1.0.ADMIN-UIMIG-234")
            revAsJson.CHEI212.lastRevision.toString() == "234"
            revAsJson.CHTI211.revisions.size() == 1
            revAsJson.CHTI211.revisions.contains("9.1.0.ADMIN-UIMIG-15")
            revAsJson.CHTI211.lastRevision.toString() == "15"
        when:
            result = gradleRunnerFactory(['init', 'saveRevision', '-Ptarget=chti211', '-Prevision=18', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            result = gradleRunnerFactory(['init', 'saveRevision', '-Ptarget=chei212', '-Prevision=77', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            revAsJson = new JsonSlurper().parse(revFile)
        then:
            revFile.exists()
            revAsJson.nextRev == 1 // because we started with a brand new Revision -> although it has no meaning for this particular test
            revAsJson.prodRevision == null // because we started with a brand new Revision -> although it has no meaning for this particular test
            revAsJson.CHEI212.revisions.size() == 3
            revAsJson.CHEI212.revisions.contains("9.1.0.ADMIN-UIMIG-123")
            revAsJson.CHEI212.revisions.contains("9.1.0.ADMIN-UIMIG-234")
            revAsJson.CHEI212.revisions.contains("9.1.0.ADMIN-UIMIG-77")
            revAsJson.CHEI212.lastRevision.toString() == "77"
            revAsJson.CHTI211.revisions.size() == 2
            revAsJson.CHTI211.revisions.contains("9.1.0.ADMIN-UIMIG-15")
            revAsJson.CHTI211.revisions.contains("9.1.0.ADMIN-UIMIG-18")
            revAsJson.CHTI211.lastRevision.toString() == "18"
        cleanup:
            revFile.delete()
    }
}
