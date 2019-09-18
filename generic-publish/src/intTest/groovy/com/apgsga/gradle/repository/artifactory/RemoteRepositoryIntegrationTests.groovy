package com.apgsga.gradle.repository.artifactory

import org.apache.commons.io.FileUtils
import org.jasypt.util.text.BasicTextEncryptor
import org.jfrog.artifactory.client.ItemHandle
import com.apgsga.gradle.repository.UploadRepository
import com.apgsga.gradle.repository.UploadRepositoryBuilder
import spock.lang.Specification

class RemoteRepositoryIntegrationTests extends Specification {

	private static final String ARTIFACTORY_URL = "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga"

	private static final String TEST_RPM_REPO = "yumdigiflexsnaprepo"

	private static final String USER = "dev"

	private static final String PASSWD = "/ikoI8/CA/EwaNqnDBfrtA=="

	private static final String TEST_RPM_NAME = "apg-plugintests-0.8.9-1.noarch.rpm"
	
	BasicTextEncryptor textEncryptor;
		
	def setup() {
		textEncryptor = new BasicTextEncryptor();
		// TODO (che, 17.9 ) : externalize
		textEncryptor.password = "repo"
		RemoteRepositoryBuilder builder = RemoteRepositoryBuilder.create(ARTIFACTORY_URL);
		builder.targetRepo = TEST_RPM_REPO
		builder.username = USER
		builder.password = textEncryptor.decrypt(PASSWD)
		RemoteRepository repo = builder.build();
		repo.delete(TEST_RPM_NAME);
	}
	
	def "publish rpm to remote rpm repo works"() {
		given:
			RemoteRepositoryBuilder builder = RemoteRepositoryBuilder.create(ARTIFACTORY_URL);
		    File rpmToPublish = new File("src/intTest/resources/"+ TEST_RPM_NAME)
			builder.targetRepo = TEST_RPM_REPO
		    builder.username = USER 
			builder.password = textEncryptor.decrypt(PASSWD)
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
			RemoteRepositoryBuilder builder = RemoteRepositoryBuilder.create(ARTIFACTORY_URL);
			File rpmToPublish = new File("src/intTest/resources/"+ TEST_RPM_NAME)
			builder.targetRepo = TEST_RPM_REPO
			builder.username = USER
			builder.password = textEncryptor.decrypt(PASSWD)
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
	

}