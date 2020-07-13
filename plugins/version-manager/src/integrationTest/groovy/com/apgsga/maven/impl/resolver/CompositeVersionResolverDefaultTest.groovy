package com.apgsga.maven.impl.resolver

import org.gradle.util.GFileUtils
import spock.lang.Shared
import spock.lang.Specification

class CompositeVersionResolverDefaultTest extends Specification {

    private static final String REPO_URL = 'build/repo'

    private static final String TEST_REPO = 'bom-test'

    @Shared
    def source = new File("src/integrationTest/resources/patchresolvertests")

    def setup() {
        def source = new File("src/integrationTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
    }

    def "GetVersion with one Bom Version Resolver, with nested Bom recursive"() {
        given:
        def mavenRecommenderBuilder = ResolverBuilderKt.create(BomVersionResolverBuilder.class)
                .recursive(true)
                .bomArtifact('test:test-nested-bom:1.1')
                .repoBaseUrl(REPO_URL)
                .repoName(TEST_REPO)
        def compositeResolver = ResolverBuilderKt.create(CompositeVersionResolverBuilder.class).
                add(1,mavenRecommenderBuilder)
                .build()
        when:
        def version = compositeResolver.getVersion("org.apache.httpcomponents", "httpclient")
        then:
        version == "4.5.2"

    }

}
