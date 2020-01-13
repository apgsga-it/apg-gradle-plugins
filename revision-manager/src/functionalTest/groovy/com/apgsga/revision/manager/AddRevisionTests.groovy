package com.apgsga.revision.manager

import com.apgsga.gradle.test.utils.AbstractSpecification

class AddRevisionTests extends AbstractSpecification {

    def "Assert add Revision works"() {
        given:
            buildFile << """
                        plugins {
                            id 'com.apgsga.revision.manager' 
                        }
                    """
        when:
            def result = gradleRunnerFactory(['init', 'addRevision']).build()
        then:
            println "Result output: ${result.output}"
    }
}
