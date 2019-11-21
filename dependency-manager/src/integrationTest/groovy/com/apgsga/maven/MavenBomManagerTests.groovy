package com.apgsga.maven

import org.gradle.util.GFileUtils
import spock.lang.Specification

class MavenBomManagerTests extends Specification {

	private static final String REPO_URL = "build/repo"

	private static final String TEST_REPO = "bom-test"

	private static final String BOM_NAME = "test-simple-bom-pom.xml"

	def "load simple Bom from Repository"() {
		given:
			def BOM_NAME = "test-simple-bom-pom.xml"
			def source = new File("src/integrationTest/resources/bomfiles/$BOM_NAME")
			def destination = new File("$REPO_URL/$TEST_REPO/$BOM_NAME")
			GFileUtils.copyFile(source,destination)
			def bomManager = new MavenBomManagerDefaultImpl(REPO_URL,TEST_REPO,null,null)
		when:
			def result = bomManager.loadModel(BOM_NAME)
		then:
			result != null
			result.size() == 4
			def expectedArtifacts = ['org.apache.httpcomponents:httpclient:4.5.2:jar','com.google.code.guice:guice:4.1:jar','com.affichage.ui.utils:jgoodies-utils:1.9.6:jar','com.affichage.it21.ppix:ppix-dao:9.0.6.ADMIN-UIMIG-SNAPSHOT:jar']
			result.each {
				def artString = "${it.groupId}:${it.artifactid}:${it.version}:${it.type}".toString()
				expectedArtifacts.remove(artString)
			}
			expectedArtifacts.size() == 0

	}
	
	


}