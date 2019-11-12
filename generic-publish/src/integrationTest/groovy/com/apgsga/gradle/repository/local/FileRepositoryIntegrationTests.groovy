package com.apgsga.gradle.repository.local

import org.apache.commons.io.FileUtils

import com.apgsga.gradle.repository.Repository
import com.apgsga.gradle.repository.RepositoryBuilder
import org.apache.commons.io.IOUtils
import spock.lang.Specification

class FileRepositoryIntegrationTests extends Specification {

	def setup() {
		File repo = new File("build/testrepo")
		FileUtils.deleteQuietly(repo)
	}

	def "publish to local repo works"() {
		given:
		File rpmToPublish = new File("src/integrationTest/resources/apg-plugintests-0.8.9-1.noarch.rpm")
		RepositoryBuilder builder = FileRepositoryBuilder.create("build")
		builder.targetRepo = "testrepo"
		Repository uploadRepo = builder.build()
		when:
		def deploy = uploadRepo.upload(rpmToPublish.getName(), rpmToPublish)
		then:
		!deploy.isFolder()
		deploy.name == "apg-plugintests-0.8.9-1.noarch.rpm"
		deploy.repo == "build/testrepo"
	}

	def "publish repo does'nt exist"() {
		given:
		RepositoryBuilder builder = FileRepositoryBuilder.create("x")
		builder.targetRepo = "testrepo"
		when:
		builder.build()
		then:
		IllegalStateException ex = thrown()
		ex.getMessage() == "Base Repository Directory doesn't exist:  [x]"
	}

	def "publish testrepo is file"() {
		given:
		File testFile = new File("build/testrepo")
		testFile.write "First Line\n"
		testFile << "Second Line\n"
		RepositoryBuilder builder = FileRepositoryBuilder.create("build")
		builder.targetRepo = "testrepo"
		when:
		builder.build()
		then:
		IllegalStateException ex = thrown()
		ex.getMessage() == "Repo exists but isn't a directory:  [testrepo]"
	}

	def "download from Repo "() {
		given:
		File rpmToPublish = new File("src/integrationTest/resources/apg-plugintests-0.8.9-1.noarch.rpm")
		RepositoryBuilder builder = FileRepositoryBuilder.create("build")
		builder.targetRepo = "testrepo"
		Repository repo = builder.build()
		repo.upload(rpmToPublish.getName(), rpmToPublish)
		def download = repo.download("apg-plugintests-0.8.9-1.noarch.rpm")
		when:
		File downloadedFile = new File("build/result.rpm")
		FileUtils.copyInputStreamToFile(download, downloadedFile)
		then:
		rpmToPublish.size() == downloadedFile.size()
	}
}