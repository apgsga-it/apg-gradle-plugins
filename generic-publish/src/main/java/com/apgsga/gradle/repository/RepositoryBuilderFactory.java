package com.apgsga.gradle.repository;

import com.apgsga.gradle.repository.artifactory.RemoteRepositoryBuilder;
import com.apgsga.gradle.repository.local.FileRepositoryBuilder;
import com.apgsga.gradle.repository.nop.NopRepositoryBuilder;

public class RepositoryBuilderFactory {
	
	public static UploadRepositoryBuilder createFor(String baseUrl, boolean publish) {
		if (!publish)  {
			return NopRepositoryBuilder.NOP;
		}
		if (baseUrl.startsWith("http")) {
			return RemoteRepositoryBuilder.create(baseUrl); 
		}
		return FileRepositoryBuilder.create(baseUrl); 
	}

}
