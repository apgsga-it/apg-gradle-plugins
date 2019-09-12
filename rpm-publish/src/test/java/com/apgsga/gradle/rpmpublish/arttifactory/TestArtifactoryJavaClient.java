package com.apgsga.gradle.rpmpublish.arttifactory;

import java.util.List;

import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.model.Item;
import org.jfrog.artifactory.client.model.PackageType;
import org.jfrog.artifactory.client.model.RepoPath;
import org.jfrog.artifactory.client.model.Repository;
import org.jfrog.artifactory.client.model.RepositoryType;
import org.jfrog.artifactory.client.model.repository.settings.RepositorySettings;

public class TestArtifactoryJavaClient {

	static private String DIGIFLEX_SNAPSHOT_REPO = "yumdigiflexsnaprepo";

	public static void main(String[] args) {
		// Connection
		Artifactory artifactory = ArtifactoryClientBuilder.create()
				.setUrl("https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga").setUsername("dev")
				.setPassword("dev1234").build();
		System.out.println("Digiflex Repo Info: ");

		// Query Repository
		Repository repo = artifactory.repository(DIGIFLEX_SNAPSHOT_REPO).get();
		String repoKey = repo.getKey();
		System.out.println("repoKey: " + repoKey);
		String desc = repo.getDescription();
		System.out.println("desc: " + desc);
		String layout = repo.getRepoLayoutRef();
		System.out.println("layout: " + layout);
		RepositoryType repoClass = repo.getRclass();
		System.out.println("repoClass: " + repoClass);
		RepositorySettings settings = repo.getRepositorySettings();
		PackageType packageType = settings.getPackageType();
		System.out.println("packageType: " + packageType.toString());

		// Query Repo with filter
		List<RepoPath> searchItems = artifactory.searches().repositories(DIGIFLEX_SNAPSHOT_REPO)
				.artifactsByName("apg-*service-*.*").doSearch();

		for (RepoPath searchItem : searchItems) {
			String repoKeySearch = searchItem.getRepoKey();
			System.out.println("Repo key: " + repoKeySearch);
			String itemPath = searchItem.getItemPath();
			System.out.println("ItemPath: " + itemPath);
			// Info
			Item info = artifactory.repository(DIGIFLEX_SNAPSHOT_REPO).file(itemPath).info();
			System.out.println("Item Info: " + info.toString());
			// Delete
		    String result = artifactory.repository(DIGIFLEX_SNAPSHOT_REPO).delete(itemPath);
		    System.out.println("Deleted: " + result); 

		}

	}

}
