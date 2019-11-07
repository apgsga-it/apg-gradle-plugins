package com.apgsga.gradle.repository.local

import org.apache.commons.io.FileUtils

import com.apgsga.gradle.repository.UploadRepository
import com.apgsga.gradle.repository.UploadRepositoryBuilder
import spock.lang.Specification

class FileRepositoryIntegrationTests extends Specification {
	
	def setup() {
		File repo = new File("build/testrepo")
		FileUtils.deleteQuietly(repo)
	}
	
	def "publish to local repo works"() {
		given:
		File rpmToPublish = new File("src/integrationTest/resources/apg-plugintests-0.8.9-1.noarch.rpm")
		UploadRepositoryBuilder builder = FileRepositoryBuilder.create("build")
		builder.targetRepo = "testrepo"
		UploadRepository uploadRepo = builder.build()
		when:
			def deploy = uploadRepo.upload(rpmToPublish.getName(),rpmToPublish)
		then:
			!deploy.isFolder()
			deploy.name == "apg-plugintests-0.8.9-1.noarch.rpm"
			deploy.repo == "build/testrepo"
	}
	
	def "publish repo does'nt exist"() {
		given:
		UploadRepositoryBuilder builder = FileRepositoryBuilder.create("x")
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
		testFile.write  "First Line\n"
		testFile << "Second Line\n"
		UploadRepositoryBuilder builder = FileRepositoryBuilder.create("build")
		builder.targetRepo = "testrepo"
		when:
			builder.build()
		then:
		   IllegalStateException ex = thrown()
		   ex.getMessage() == "Repo exists but isn't a directory:  [testrepo]"
	}
}