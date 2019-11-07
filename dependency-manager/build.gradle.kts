apply {
    from ("${projectDir}/../gradle-common/common-plugin.gradle")
    from ("${projectDir}/../gradle-common/integration-test.gradle")
    from ("${projectDir}/../gradle-common/functional-test.gradle")
}

plugins {
    `kotlin-dsl`
}

dependencies {
    testCompile(gradleTestKit())
    testCompile(project(":common-test"))  {
        exclude("","groovy-all")
    }
    compile (project(":common-repo"))
}
