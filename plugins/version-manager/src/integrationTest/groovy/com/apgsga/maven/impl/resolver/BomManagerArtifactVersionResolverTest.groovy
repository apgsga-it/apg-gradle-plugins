package com.apgsga.maven.impl.resolver


import org.gradle.util.GFileUtils
import spock.lang.Ignore
import spock.lang.Specification
// TODO (che , jhe , 8.9: these tests need to move to functionaltests , since for resolving boms the Gradle proper Resolver is used"

class BomManagerArtifactVersionResolverTest extends Specification {

    private static final String REPO_URL = 'build/repo'

    private static final String TEST_REPO = 'bom-test'

    def setup() {
        def source = new File("src/integrationTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
    }

    @Ignore()
    def "getVersion Simple Test with nested Bom recursive"() {
        given:
        def mavenRecommender = ResolverBuilderKt.create(BomVersionResolverBuilder.class)
                .recursive(true)
                .bomArtifact('test:test-nested-bom:1.1')
                .repoBaseUrl(REPO_URL)
                .repoName(TEST_REPO)
                .build()
        when:
        def version = mavenRecommender.getVersion("org.apache.httpcomponents", "httpclient")
        then:
        version == "4.5.2"

    }

    @Ignore()
    def "getVersion Simple Test with nested Bom not recursive"() {
        given:
        def mavenRecommender = ResolverBuilderKt.create(BomVersionResolverBuilder.class)
                .recursive(false)
                .bomArtifact('test:test-nested-bom:1.1')
                .repoBaseUrl(REPO_URL)
                .repoName(TEST_REPO)
                .build()
        when:
        def version = mavenRecommender.getVersion("org.apache.httpcomponents", "httpclient")
        then:
        version == ""

    }
}
