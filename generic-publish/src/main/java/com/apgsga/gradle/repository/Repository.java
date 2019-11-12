package com.apgsga.gradle.repository;

import java.io.File;
import java.io.InputStream;

public interface Repository {

	boolean exists();

	Result upload(String fileName, File fileToUpload);

	InputStream download(String relativePath);
	

}