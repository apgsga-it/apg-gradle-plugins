package com.apgsga.gradle.rpmpublish.arttifactory;

import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.model.File;

public class TestArtifactoryJavaClientUpload {

	static private String DIGIFLEX_SNAPSHOT_REPO = "yumdigiflexsnaprepo";

	public static void main(String[] args) {
		// Connection
		ArtifactoryClientBuilder builder = ArtifactoryClientBuilder.create();
		Artifactory artifactory = builder
				.setUrl("https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga").setUsername("dev")
				.setPassword("xxxx")
				.build();
		java.io.File file = new java.io.File("src/test/resources/apg-interweb-service-CHEI212-0.8.9-1.noarch.rpm");
		File deployed = artifactory.repository(DIGIFLEX_SNAPSHOT_REPO).upload(file.getName(), file)
				.withProperty("color", "blue").withProperty("color", "red").doUpload();
		System.out.println("Deployed file: " + deployed.toString());

	}

}
