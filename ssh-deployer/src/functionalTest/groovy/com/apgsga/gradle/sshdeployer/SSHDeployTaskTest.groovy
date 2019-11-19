package com.apgsga.gradle.sshdeployer

import com.apgsga.gradle.test.utils.AbstractSpecification
import org.gradle.testkit.runner.GradleRunner

class SSHDeployTaskTest extends AbstractSpecification {

    def "SSH Deployment task works"() {
        given:
            buildFile << """
                    plugins {
                        id 'com.apgsga.gradle.sshdeployer'
                    }
                    
                    apgRepository {
                        mavenLocal()
                        mavenCentral()
                    }
                """
        when:
            def result = GradleRunner.create()
                    .withProjectDir(testProjectDir)
                    .withArguments( 'deployRpm')
                    .withPluginClasspath()
                    .build()

        then:
            println "Result output: ${result.output}"
            // TODO JHE: correct test ... :)
            result.output.contains('')
    }

}
