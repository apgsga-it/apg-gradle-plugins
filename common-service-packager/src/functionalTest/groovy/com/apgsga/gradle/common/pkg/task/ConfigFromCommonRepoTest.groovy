package com.apgsga.gradle.common.pkg.task

import com.apgsga.gradle.test.utils.AbstractSpecification
import spock.lang.Shared

import static groovy.io.FileType.FILES

class ConfigFromCommonRepoTest extends AbstractSpecification {

    def "repoBaseUrl config works"() {
        given:
        buildFile << """
                plugins {
                    id 'com.apgsga.common.package'
                }
                println apgPackage.toString()
            """

        when:
        def result = gradleRunnerFactory(['copyCommonPackagingResources']).build()
        then:
        // TODO JHE: well, not a very good test, but it at least ensure the default URL from common-repo has correctly been retrieved.
        println result.output
        result.output.contains("distRepoUrl='https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga/apgPlatformDependencies-test'")
    }
}