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

    def "GetVersion with one Bom Version Resolver and PatchFile Resolver with Artifact from Bom"() {
        given:
        def mavenRecommenderBuilder = ResolverBuilderKt.create(BomVersionResolverBuilder.class)
                .recursive(true)
                .bomArtifact('test:test-nested-bom:1.1')
                .repoBaseUrl(REPO_URL)
                .repoName(TEST_REPO)
        def patchFileResolverBuilder = ResolverBuilderKt.create(PatchFileVersionResolverBuilder.class)
                .parentDir(source)
                .patchFile("PatchZ5401.json")
        def compositeResolver = ResolverBuilderKt.create(CompositeVersionResolverBuilder.class)
                .add(2,mavenRecommenderBuilder)
                .add(1,patchFileResolverBuilder)
                .build()
        when:
        def version = compositeResolver.getVersion("org.apache.httpcomponents", "httpclient")
        then:
        version == "4.5.2"

    }
    def "GetVersion with one Bom Version Resolver and PatchFile Resolver, with artifact from Patch"() {
        given:
        def mavenRecommenderBuilder = ResolverBuilderKt.create(BomVersionResolverBuilder.class)
                .recursive(true)
                .bomArtifact('test:test-nested-bom:1.1')
                .repoBaseUrl(REPO_URL)
                .repoName(TEST_REPO)
        def patchFileResolverBuilder = ResolverBuilderKt.create(PatchFileVersionResolverBuilder.class)
                .parentDir(source)
                .patchFile("PatchZ5401.json")
        def compositeResolver = ResolverBuilderKt.create(CompositeVersionResolverBuilder.class)
                .add(2,mavenRecommenderBuilder)
                .add(1,patchFileResolverBuilder)
                .build()
        when:
        def version = compositeResolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"

    }

    def "GetVersion with one Bom Version Resolver and PatchFile Resolver ordered, with same artifact but different versions"() {
        given:
        def mavenRecommenderBuilder = ResolverBuilderKt.create(BomVersionResolverBuilder.class)
                .recursive(true)
                .bomArtifact('test:test-composite-bom:1.0')
                .repoBaseUrl(REPO_URL)
                .repoName(TEST_REPO)
        def patchFileResolverBuilder = ResolverBuilderKt.create(PatchFileVersionResolverBuilder.class)
                .parentDir(source)
                .patchFile("PatchA5791.json")
        def compositeResolver = ResolverBuilderKt.create(CompositeVersionResolverBuilder.class)
                .add(2,mavenRecommenderBuilder)
                .add(1,patchFileResolverBuilder)
                .build()
        when:
        def version = compositeResolver.getVersion("com.affichage.it21.alog","alog-ui")
        then:
        version == "12"

    }

    def "GetVersion with one Bom Version Resolver and PatchFile Resolver ordered different with same artifact but different versions"() {
        given:
        def mavenRecommenderBuilder = ResolverBuilderKt.create(BomVersionResolverBuilder.class)
                .recursive(true)
                .bomArtifact('test:test-composite-bom:1.0')
                .repoBaseUrl(REPO_URL)
                .repoName(TEST_REPO)
        def patchFileResolverBuilder = ResolverBuilderKt.create(PatchFileVersionResolverBuilder.class)
                .parentDir(source)
                .patchFile("PatchA5791.json")
        def compositeResolver = ResolverBuilderKt.create(CompositeVersionResolverBuilder.class)
                .add(1,mavenRecommenderBuilder)
                .add(2,patchFileResolverBuilder)
                .build()
        when:
        def version = compositeResolver.getVersion("com.affichage.it21.alog","alog-ui")
        then:
        version == "9.0.6.ADMIN-UIMIG-SNAPSHOT"

    }
}
