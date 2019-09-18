package com.apgsga.gradle.repository;

import java.util.Date;

public interface UploadResult {

	Date getCreated();

	String getCreatedBy();

	String getDownloadUri();

	Date getLastModified();

	Date getLastUpdated();

	String getName();

	String getPath();

	String getRepo();

	long getSize();

	String getUri();

	boolean isFolder();

}