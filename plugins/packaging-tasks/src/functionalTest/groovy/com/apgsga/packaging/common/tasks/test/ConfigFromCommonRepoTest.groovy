package com.apgsga.packaging.common.tasks.test

import com.apgsga.gradle.test.utils.AbstractSpecification
import com.apgsga.packaging.plugins.ApgServicePackagePlugin

class ConfigFromCommonRepoTest extends AbstractSpecification {

    def "repoBaseUrl config works"() {
        given:
        buildFile << """
                plugins {
                    id 'com.apgsga.service.package'
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