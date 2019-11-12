package com.apgsga.gradle.repository.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import com.apgsga.gradle.repository.Result;

public class FileResult implements Result {

	private File uploadResultFile;

	FileResult(File targetFile) {
		this.uploadResultFile = targetFile;
	}

	@Override
	public Date getCreated() {
		BasicFileAttributes attr = getBasicAttr();
		FileTime creationTime = attr.creationTime();
		return new Date(creationTime.toMillis());
	}

	private BasicFileAttributes getBasicAttr() {
		BasicFileAttributes attr;
		try {
			attr = Files.readAttributes(uploadResultFile.toPath(), BasicFileAttributes.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return attr;
	}

	@Override
	public String getCreatedBy() {
		return "TODO";

	}

	@Override
	public String getDownloadUri() {
		return uploadResultFile.getAbsolutePath();
	}

	@Override
	public Date getLastModified() {
		BasicFileAttributes attr = getBasicAttr();
		FileTime time = attr.lastModifiedTime();
		return new Date(time.toMillis());
	}

	@Override
	public Date getLastUpdated() {
		return getLastModified();
	}

	@Override
	public String getName() {
		return uploadResultFile.getName();
	}

	@Override
	public String getPath() {
		return uploadResultFile.getPath();
	}

	@Override
	public String getRepo() {
		return uploadResultFile.getParent();
	}

	@Override
	public long getSize() {
		BasicFileAttributes attr = getBasicAttr();
		return attr.size();
	}

	@Override
	public String getUri() {
		return uploadResultFile.toURI().toASCIIString();
	}

	@Override
	public boolean isFolder() {
		return uploadResultFile.isDirectory();
	}

	@Override
	public String toString() {
		return "FileUploadResult [uploadResultFile=" + uploadResultFile + "]";
	}


	
	

}
