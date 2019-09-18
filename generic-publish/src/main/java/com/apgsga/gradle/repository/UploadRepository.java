package com.apgsga.gradle.repository;

import java.io.File;

public interface UploadRepository {

	boolean exists();

	UploadResult upload(String fileName, File fileToUpload);
	

}