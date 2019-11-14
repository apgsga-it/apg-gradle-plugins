package com.apgsga.maven

import org.gradle.util.GFileUtils
import spock.lang.Specification

class MavenBomManagerTests extends Specification {

	private static final String REPO_URL = "build/repo"

	private static final String TEST_REPO = "bom-test"

	private static final String BOM_NAME = "test-bom-pom.xml"

	def setup() {
		def source = new File("src/integrationTest/resources/$BOM_NAME")
		def destination = new File("$REPO_URL/$TEST_REPO/$BOM_NAME")
		GFileUtils.copyFile(source,destination)
	}

	def "load Bom from Repository"() {
		given:
			def bomManager = new MavenBomManagerDefaultImpl(REPO_URL,TEST_REPO,null,null)
		when:
			def result = bomManager.loadModel(BOM_NAME)
		then:
			result != null
			println (result.toString())
	}
	
	


}