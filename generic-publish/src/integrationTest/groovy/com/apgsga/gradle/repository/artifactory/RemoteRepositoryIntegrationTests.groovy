package com.apgsga.gradle.repository.artifactory

import org.apache.commons.io.FileUtils
import org.jasypt.util.text.BasicTextEncryptor
import org.jfrog.artifactory.client.ItemHandle
import com.apgsga.gradle.repository.UploadRepository
import com.apgsga.gradle.repository.UploadRepositoryBuilder
import spock.lang.Specification

class RemoteRepositoryIntegrationTests extends Specification {

	private static final String ARTIFACTORY_URL = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga"

	private static final String TEST_RPM_REPO = "rpm-functionaltest"

	private static final String USER = "gradledev-tests-user"

	private static final String TEST_RPM_NAME = "apg-plugintests-0.8.9-1.noarch.rpm"
	
	private static final String TEST_TARBALL_NAME = "app-pkg.tar.gz"
	
	private static final String TEST_TARBALL_REPO = "generic-functionaltest"

	private static final String SOMEAPP_PKG_TAR_GZ_NAME = "someapp-pkg.tar.gz"

		
	def setupSpec() {
		RemoteRepositoryBuilder builder = RemoteRepositoryBuilder.create(ARTIFACTORY_URL)
		builder.targetRepo = TEST_RPM_REPO
		builder.username = USER
		builder.password = USER
		RemoteRepository repo = builder.build()
		try { 
			repo.delete(TEST_RPM_NAME)
		} catch (Exception ignored) {
			// Ignored
		}
		try {
			repo.delete(SOMEAPP_PKG_TAR_GZ_NAME)
		} catch (Exception ignored) {
			
		}
	}
	
	def "publish rpm to remote rpm repo works"() {
		given:
			RemoteRepositoryBuilder builder = RemoteRepositoryBuilder.create(ARTIFACTORY_URL)
		    File rpmToPublish = new File("src/integrationTest/resources/"+ TEST_RPM_NAME)
			builder.targetRepo = TEST_RPM_REPO
		    builder.username = USER 
			builder.password = USER
	        UploadRepository uploadRepo = builder.build()
		when:
			def deploy = uploadRepo.upload(rpmToPublish.getName(),rpmToPublish)
		then:
			!deploy.isFolder()
			deploy.name == TEST_RPM_NAME
			deploy.path == "/" + TEST_RPM_NAME
			deploy.repo == TEST_RPM_REPO
			deploy.uri ==   ARTIFACTORY_URL + "/" + TEST_RPM_REPO + "/" + TEST_RPM_NAME
	}
	
	
	def "publish tar.gz to remote generic repo works"() {
		given:
			RemoteRepositoryBuilder builder = RemoteRepositoryBuilder.create(ARTIFACTORY_URL)
			File toPublish = new File("src/integrationTest/resources/"+ TEST_TARBALL_NAME)
			builder.targetRepo = TEST_TARBALL_REPO
			builder.username = USER
			builder.password = USER
			UploadRepository uploadRepo = builder.build()
		when:
			def deploy = uploadRepo.upload(SOMEAPP_PKG_TAR_GZ_NAME,toPublish)
		then:
			!deploy.isFolder()
			deploy.name == SOMEAPP_PKG_TAR_GZ_NAME
			deploy.path == "/" + SOMEAPP_PKG_TAR_GZ_NAME
			deploy.repo == TEST_TARBALL_REPO
			deploy.uri ==   ARTIFACTORY_URL + "/" + TEST_TARBALL_REPO + "/" + SOMEAPP_PKG_TAR_GZ_NAME
	}
	

}