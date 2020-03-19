package com.apgsga.ssh.extensions

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.ssh.plugins.ApgSsh

class BuildLogicSshConfigurationFunctionalTests extends AbstractSpecification {

    def "SSH properties configuration works"() {
        given:
            def apgSshPluginId = ApgSsh.PLUGIN_ID
            buildFile << """
                            plugins {
                                id '${apgSshPluginId}' 
                            }
                            
                            apgSshConfig {
                                username 'bob'
                                userpwd 'bobPw'
                                destinationHost 'ourRemoteHost'
                            }				
                            apgSshConfig.log()
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
