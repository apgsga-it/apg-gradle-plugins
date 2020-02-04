package com.apgsga.gradle.repository.artifactory


import com.apgsga.gradle.repository.Repository
import org.apache.commons.io.FileUtils
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
	        Repository uploadRepo = builder.build()
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
			Repository uploadRepo = builder.build()
		when:
			def deploy = uploadRepo.upload(SOMEAPP_PKG_TAR_GZ_NAME,toPublish)
		then:
			!deploy.isFolder()
			deploy.name == SOMEAPP_PKG_TAR_GZ_NAME
			deploy.path == "/" + SOMEAPP_PKG_TAR_GZ_NAME
			deploy.repo == TEST_TARBALL_REPO
			deploy.uri ==   ARTIFACTORY_URL + "/" + TEST_TARBALL_REPO + "/" + SOMEAPP_PKG_TAR_GZ_NAME
	}

	def "download pom.xml from artifactory works"() {
		given:
		RemoteRepositoryBuilder builder = RemoteRepositoryBuilder.create(ARTIFACTORY_URL)
		builder.targetRepo = "snapshots"
		builder.username = USER
		builder.password = USER
		Repository repo = builder.build()
		when:
		def download = repo.download("com/affichage/common/maven/dm-bom/9.0.6.ADMIN-UIMIG-SNAPSHOT/dm-bom-9.0.6.ADMIN-UIMIG-SNAPSHOT.pom")
		File downloadedFile = new File("build/result.pom")
		FileUtils.copyInputStreamToFile(download, downloadedFile)
		then:
		downloadedFile.isFile()
		def pom = new XmlParser().parse(downloadedFile)
		pom.properties.'*'.size() > 20
		pom.dependencyManagement.dependencies.'*'.size() > 20
	}
	

}