package com.apgsga.gradle.repository;

import com.apgsga.gradle.repository.artifactory.RemoteRepositoryBuilder;
import com.apgsga.gradle.repository.local.FileRepositoryBuilder;

public class RepositoryBuilderFactory {
	
	public static UploadRepositoryBuilder createFor(String baseUrl) {
		if (baseUrl.startsWith("http")) {
			return RemoteRepositoryBuilder.create(baseUrl); 
		}
		return FileRepositoryBuilder.create(baseUrl); 
	}

}
