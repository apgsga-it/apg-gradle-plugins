package com.apgsga.maven

import org.gradle.util.GFileUtils
import spock.lang.Specification

class MavenBomManagerTests extends Specification {

    private static final String REPO_URL = "build/repo"

    private static final String TEST_REPO = "bom-test"

    def setup() {
        def source = new File("src/integrationTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
    }

    def "load simple Bom from Repository"() {
        given:

        def bomManager = new MavenBomManagerDefaultImpl(REPO_URL, TEST_REPO, null, null)
        when:
        def result = bomManager.retrieve("test", "test-bom", "1.0", true)
        then:
        result != null
        result.size() == 4
        def expectedArtifacts = ['org.apache.httpcomponents:httpclient:4.5.2:jar', 'com.google.code.guice:guice:4.1:jar', 'com.affichage.ui.utils:jgoodies-utils:1.9.6:jar', 'com.affichage.it21.ppix:ppix-dao:9.0.6.ADMIN-UIMIG-SNAPSHOT:jar']
        result.each {
            def artString = "${it.groupId}:${it.artifactid}:${it.version}:${it.type}".toString()
            expectedArtifacts.remove(artString)
        }
        expectedArtifacts.size() == 0

    }

    def "load nested Bom from Repository"() {
        given:
        def bomManager = new MavenBomManagerDefaultImpl(REPO_URL, TEST_REPO, null, null)
        when:
        def result = bomManager.retrieve("test", "test-nested-bom", "1.1", true)
        then:
        result != null
        result.size() == 4
        def expectedArtifacts = ['org.apache.httpcomponents:httpclient:4.5.2:jar', 'com.google.code.guice:guice:4.1:jar', 'com.affichage.ui.utils:jgoodies-utils:1.9.6:jar', 'com.affichage.it21.ppix:ppix-dao:9.0.6.ADMIN-UIMIG-SNAPSHOT:jar']
        result.each {
            def artString = "${it.groupId}:${it.artifactid}:${it.version}:${it.type}".toString()
            expectedArtifacts.remove(artString)
        }
        expectedArtifacts.size() == 0

    }

	def "load nested Bom with some more artifacts from Repository"() {
		given:
		def bomManager = new MavenBomManagerDefaultImpl(REPO_URL, TEST_REPO, null, null)
		when:
		def result = bomManager.retrieve("test", "test-nested-withadd-bom", "1.2", true)
		then:
		result != null
		result.size() == 5
		def expectedArtifacts = ['org.test:jamesbond:0.0.7:jar','org.apache.httpcomponents:httpclient:4.5.2:jar', 'com.google.code.guice:guice:4.1:jar', 'com.affichage.ui.utils:jgoodies-utils:1.9.6:jar', 'com.affichage.it21.ppix:ppix-dao:9.0.6.ADMIN-UIMIG-SNAPSHOT:jar']
		result.each {
			def artString = "${it.groupId}:${it.artifactid}:${it.version}:${it.type}".toString()
			expectedArtifacts.remove(artString)
		}
		expectedArtifacts.size() == 0

	}

    def "load nested Bom non recursive from Repository"() {
        given:
        def bomManager = new MavenBomManagerDefaultImpl(REPO_URL, TEST_REPO, null, null)
        when:
        def result = bomManager.retrieve("test", "test-nested-bom", "1.1", false)
        then:
        result != null
        result.size() == 0

    }


    def "load nested Bom with some more artifacts  no recursive from Repository"() {
        given:
        def bomManager = new MavenBomManagerDefaultImpl(REPO_URL, TEST_REPO, null, null)
        when:
        def result = bomManager.retrieve("test", "test-nested-withadd-bom", "1.2", true)
        then:
        result != null
        result.size() == 5
        def expectedArtifacts = ['org.test:jamesbond:0.0.7:jar']
        result.each {
            def artString = "${it.groupId}:${it.artifactid}:${it.version}:${it.type}".toString()
            expectedArtifacts.remove(artString)
        }
        expectedArtifacts.size() == 0

    }




}