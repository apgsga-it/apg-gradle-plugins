package com.apgsga.ssh.common

import com.apgsga.gradle.test.utils.AbstractSpecification

class BuildLogicFunctionalTests extends AbstractSpecification {

    def "SSH properties configuration works"() {
        given:
            buildFile << """
                        plugins {
                            id 'com.apgsga.ssh.common' 
                        }
                        
                        apgSshCommon {
                            username 'bob'
                            userpwd 'bobPw'
                            destinationHost 'ourRemoteHost'
                        }				
                        apgSshCommon.log()
                    """
        when:
            def result = gradleRunnerFactory(['init']).build()
        then:
            println "Result output: ${result.output}"
            result.output.contains("destinationHost='ourRemoteHost'")
            result.output.contains("username='bob'")
            result.output.contains("userpwd='xxxx'")
    }
}
