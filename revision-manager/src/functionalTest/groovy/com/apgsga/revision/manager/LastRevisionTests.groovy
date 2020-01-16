package com.apgsga.revision.manager

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.revision.manager.tasks.LastRevision
import com.fasterxml.jackson.databind.util.ISO8601Utils

class LastRevisionTests extends AbstractSpecification {

    def "Validate last revision without any parameter"() {
        given:
            buildFile << """
                                plugins {
                                    id 'com.apgsga.revision.manager' 
                                }
                            """
            def revFile = new File("src/functionalTest/resources/Revisions.json")
        when:
            def result = gradleRunnerFactory(['init', 'lastRevision']).build()
        then:
            // JHE: mmhh, a bit generic ;), but really important to test here is that we get an Exception
            thrown Exception
        cleanup:
            revFile.delete()
    }

    def "Validate get last revision for a given target"() {
        given:
            buildFile << """
                                plugins {
                                    id 'com.apgsga.revision.manager' 
                                }
                                
                                def rev
                                
                                task getLastRev {
                                    dependsOn 'lastRevision'
                                        doLast {
                                            rev = tasks.lastRevision.lastRev
                                            println 'rev = ' + rev
                                        }
                                }
                                
                            """
            def revFile = new File("src/functionalTest/resources/Revisions.json")
        when:
            gradleRunnerFactory(['init', 'addRevision', '-Ptarget=chti211', '-Prevision=18', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            gradleRunnerFactory(['init', 'addRevision', '-Ptarget=chei212', '-Prevision=77', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            gradleRunnerFactory(['init', 'addRevision', '-Ptarget=chei212', '-Prevision=88', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            gradleRunnerFactory(['init', 'addRevision', '-Ptarget=chti211', '-Prevision=185', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            gradleRunnerFactory(['init', 'addRevision', '-Ptarget=chei212', '-Prevision=100', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            gradleRunnerFactory(['init', 'addRevision', '-Ptarget=chei211', '-Prevision=50', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
            // JHE: getLastRev is an example for our test build.gradle, could be any name.
            def result = gradleRunnerFactory(['init', 'getLastRev', '-Ptarget=chei212']).build()
        then:
            println result.output

//        when:
//            oldStream = System.out;
//            buffer = new ByteArrayOutputStream()
//            System.setOut(new PrintStream(buffer))
//            result = cli.process(["-lr","chti211"])
//            System.setOut(oldStream)
//        then:
//            revFile.exists()
//            result.returnCode == 0
//            buffer.toString().toString().trim() == "185"
//        when:
//            oldStream = System.out;
//            buffer = new ByteArrayOutputStream()
//            System.setOut(new PrintStream(buffer))
//            result = cli.process(["-lr","chei211"])
//            System.setOut(oldStream)
//        then:
//            revFile.exists()
//            result.returnCode == 0
//            buffer.toString().toString().trim() == "50"
//        when:
//            oldStream = System.out;
//            buffer = new ByteArrayOutputStream()
//            System.setOut(new PrintStream(buffer))
//            result = cli.process(["-lr","chti215"])
//            System.setOut(oldStream)
//        then:
//            revFile.exists()
//            result.returnCode == 0
//            buffer.toString().trim() == "SNAPSHOT"
        cleanup:
            revFile.delete()
    }
}
