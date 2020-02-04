package com.apgsga.revision.manager

import com.apgsga.gradle.test.utils.AbstractSpecification

class NextRevisionTests extends AbstractSpecification {

    def "Validate next revision without any parameter"() {
        given:
            buildFile << """
                                plugins {
                                    id 'com.apgsga.revision.manager' 
                                }
                            """
            def revFile = new File("src/functionalTest/resources/Revisions.json")
        when:
            def result = gradleRunnerFactory(['init', 'nextRevision']).build()
        then:
            // JHE: mmhh, a bit generic ;), but really important to test here is that we get an Exception
            thrown Exception
        cleanup:
            revFile.delete()
    }

    def "Validate get next revision for a given target when no revision has ever been created"() {
        given:
            buildFile << """
                                plugins {
                                    id 'com.apgsga.revision.manager' 
                                }
                                
                                def lastRev
                                def nextRev
                                
                                task getNextRev {
                                    dependsOn 'nextRevision'
                                    doFirst {
                                         lastRev = tasks.nextRevision.lastRevForTarget
                                         println 'lastRev=' + lastRev
                                         nextRev = tasks.nextRevision.nextRevForTarget
                                         println 'nextRev=' + nextRev
                                    }
                                }
                                
                            """
            def revFile = new File("src/functionalTest/resources/Revisions.json")
        when:
            // JHE: getNextRev is an example for our test build.gradle, could be any name.
            def result = gradleRunnerFactory(['init', 'getNextRev', '-Ptarget=chei212']).build()
        then:
            println result.output
            result.output.contains("lastRev=SNAPSHOT")
            result.output.contains("nextRev=1")
        cleanup:
            revFile.delete()
    }

    def "Validate get next revision for a given target with an existing Revisions.json"() {
        given:
            buildFile << """
                                    plugins {
                                        id 'com.apgsga.revision.manager' 
                                    }
                                    
                                    def lastRevForTarget
                                    def nextRevForTarget
                                    
                                    task getNextRev {
                                        dependsOn 'nextRevision'
                                        doFirst {
                                             lastRevForTarget = tasks.nextRevision.lastRevForTarget
                                             println 'lastRevForTarget=' + lastRevForTarget
                                             nextRevForTarget = tasks.nextRevision.nextRevForTarget
                                             println 'nextRevForTarget=' + nextRevForTarget
                                        }
                                    }
                                    
                                """
            def revFile = new File("src/functionalTest/resources/Revisions.json")
        when:
            // JHE: getNextRev is an example for our test build.gradle, could be any name.
            def result = gradleRunnerFactory(['init', 'getNextRev', '-Ptarget=chei212']).build()
        then:
            println result.output
            result.output.contains("lastRevForTarget=SNAPSHOT")
            result.output.contains("nextRevForTarget=1")
            gradleRunnerFactory(['init', 'saveRevision', '-Ptarget=chei212', '-Prevision=1', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
        when:
            result = gradleRunnerFactory(['init', 'getNextRev', '-Ptarget=chei212']).build()
        then:
            println result.output
            result.output.contains("lastRevForTarget=1")
            result.output.contains("nextRevForTarget=2")
            gradleRunnerFactory(['init', 'saveRevision', '-Ptarget=chei212', '-Prevision=2', '-PfullRevisionPrefix=9.1.0.ADMIN-UIMIG-']).build()
        when:
            result = gradleRunnerFactory(['init', 'getNextRev', '-Ptarget=chti211']).build()
        then:
            println result.output
            result.output.contains("lastRevForTarget=SNAPSHOT")
            result.output.contains("nextRevForTarget=3")
        cleanup:
            revFile.delete()
    }
}
