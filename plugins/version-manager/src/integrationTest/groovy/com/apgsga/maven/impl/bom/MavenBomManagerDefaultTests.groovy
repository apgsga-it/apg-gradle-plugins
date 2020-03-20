package com.apgsga.maven.impl.bom


import org.apache.maven.model.Dependency
import org.gradle.util.GFileUtils
import spock.lang.Specification

class MavenBomManagerDefaultTests extends Specification {

    private static final String REPO_URL = "build/repo"

    private static final String TEST_REPO = "bom-test"

    def bomManager

    def setup() {
        def source = new File("src/integrationTest/resources/bomtests")
        def destination = new File("$REPO_URL/$TEST_REPO")
        GFileUtils.copyDirectory(source, destination)
        def bomLoader = new RepositoryBomLoader(RepositoryFactory.createFactory(REPO_URL,TEST_REPO).makeRepo())
        bomManager = new MavenBomManagerDefault(bomLoader)

    }

    def "Retrieve model from simple Bom "() {
        when:
        def result = bomManager.retrieve("test", "test-bom", "1.0", true)
        then:
        result != null
        result.size() == 4
        expectedResult(result, ['org.apache.httpcomponents:httpclient:4.5.2:jar', 'com.google.code.guice:guice:4.1:jar', 'com.affichage.ui.utils:jgoodies-utils:1.9.6:jar', 'com.affichage.it21.ppix:ppix-dao:9.0.6.ADMIN-UIMIG-SNAPSHOT:jar'])

    }

    def "Retrieve model from nested Bom"() {
        when:
        def result = bomManager.retrieve("test", "test-nested-bom", "1.1", true)
        then:
        result != null
        result.size() == 4
        expectedResult(result, ['org.apache.httpcomponents:httpclient:4.5.2:jar', 'com.google.code.guice:guice:4.1:jar', 'com.affichage.ui.utils:jgoodies-utils:1.9.6:jar', 'com.affichage.it21.ppix:ppix-dao:9.0.6.ADMIN-UIMIG-SNAPSHOT:jar'])

    }

	def "Retrieve model from nested Bom with some additional artifacts"() {
		when:
		def result = bomManager.retrieve("test", "test-nested-withadd-bom", "1.2", true)
		then:
		result != null
		result.size() == 5
        expectedResult(result, ['org.test:jamesbond:0.0.7:jar','org.apache.httpcomponents:httpclient:4.5.2:jar', 'com.google.code.guice:guice:4.1:jar', 'com.affichage.ui.utils:jgoodies-utils:1.9.6:jar', 'com.affichage.it21.ppix:ppix-dao:9.0.6.ADMIN-UIMIG-SNAPSHOT:jar'])
	}

    def "Retrieve empty Model from  nested Bom with any own artifacts non recursively"() {
        when:
        def result = bomManager.retrieve("test", "test-nested-bom", "1.1", false)
        then:
        result != null
        result.size() == 0

    }


    def "Retrieve model from nested Bom with some own artifacts non recursively"() {
        when:
        def result = bomManager.retrieve("test", "test-nested-withadd-bom", "1.2", false)
        then:
        result != null
        result.size() == 1
        expectedResult(result,  ['org.test:jamesbond:0.0.7:jar'])
    }





    private static boolean expectedResult(Collection<Dependency> result, expectedArtifacts) {
        result.each {
            def artString = "${it.groupId}:${it.artifactId}:${it.version}:${it.type}".toString()
            expectedArtifacts.remove(artString)
        }
        expectedArtifacts.size() == 0
    }

    private static boolean expectedResultFromProperties(Properties properties, expectedArtifacts) {
        def propertyKeys = properties.keySet()
        propertyKeys.forEach() {
            def parts = "$it".split(':')
            def key = parts[0] + ":" + parts[1]
            def value = properties.getProperty("$it")
            def artString = "${key}:${value}:jar".toString()
            expectedArtifacts.remove(artString)
        }
        expectedArtifacts.size() == 0
    }




}