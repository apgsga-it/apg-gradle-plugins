package com.apgsga.gradle.ssh.plugin

import com.apgsga.gradle.test.utils.AbstractSpecification

class BuildLogicFunctionalTests extends AbstractSpecification {

    def "Basic SSH tasks and configuration works"() {
        given:
            buildFile << """
                    plugins {
                        id 'com.apgsga.gradle.ssh' 
                    }
                    apgSsh {
                        username 'bob'
                        userpassword 'bobPw'
                        target 'ourRemoteHost'
                        rpmFilePath 'build/output'
                        rpmFileName 'fakeRpm.rpm'
                        remoteDestFolder '/etc/installer'
                    }				
                    apgSsh.log()
                """
        when:
            // TODO JHE: at best we should call the deployRpm tasks with either a test server always available or a mock
            def result = gradleRunnerFactory(['init']).build()
        then:
            // TODO JHE: if we would really call deployRpm task, we could do much better asserts ...
            println "Result output: ${result.output}"
            result.output.contains("target='ourRemoteHost'")
            result.output.contains("username='bob'")
            result.output.contains("rpmFilePath='build/output'")
            result.output.contains("rpmFileName='fakeRpm.rpm'")
            result.output.contains("remoteDestFolder='/etc/installer'")
    }
}
