package com.apgsga.maven.impl.resolver

import com.apgsga.maven.impl.bom.MavenBomManagerDefault
import com.apgsga.maven.impl.bom.RepositoryFactory
import org.gradle.util.GFileUtils
import spock.lang.Specification

class BomManagerArtifactVersionResolverTest extends Specification {

    private static final String REPO_URL = 'build/repo'

    private static final String TEST_REPO = 'bom-test'

    def setup() {
        def source = new File("src/integrationTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
    }

    def "getVersion Simple Test with nested Bom recursive"() {
        given:
        def repositoryFactory = RepositoryFactory.createFactory(REPO_URL,TEST_REPO)
        def bomManager = new MavenBomManagerDefault(repositoryFactory.makeRepo())
        def mavenRecommender = new BomManagerArtifactVersionResolver('test:test-nested-bom:1.1',true,bomManager )
        when:
        def version = mavenRecommender.getVersion("org.apache.httpcomponents","httpclient")
        then:
        version == "4.5.2"

    }

    def "getVersion Simple Test with nested Bom no recursive"() {
        given:
        def repositoryFactory = RepositoryFactory.createFactory(REPO_URL,TEST_REPO)
        def bomManager = new MavenBomManagerDefault(repositoryFactory.makeRepo())
        def mavenRecommender = new BomManagerArtifactVersionResolver('test:test-nested-bom:1.1',false,bomManager )
        when:
        def version = mavenRecommender.getVersion("org.apache.httpcomponents","httpclient")
        then:
        version == ""

    }
}
