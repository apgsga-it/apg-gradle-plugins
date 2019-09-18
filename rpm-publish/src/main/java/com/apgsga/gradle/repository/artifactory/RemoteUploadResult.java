package com.apgsga.gradle.repository.artifactory;

import java.util.Date;

import org.jfrog.artifactory.client.model.Checksums;
import org.jfrog.artifactory.client.model.File;

import com.apgsga.gradle.repository.UploadResult;

public class RemoteUploadResult implements UploadResult {

	private File file;

	public RemoteUploadResult(File file) {
		super();
		this.file = file;
	}

	public Checksums getChecksums() {
		return file.getChecksums();
	}

	@Override
	public Date getCreated() {
		return file.getCreated();
	}

	@Override
	public String getCreatedBy() {
		return file.getCreatedBy();
	}

	@Override
	public String getDownloadUri() {
		return file.getDownloadUri();
	}

	@Override
	public Date getLastModified() {
		return file.getLastModified();
	}

	@Override
	public Date getLastUpdated() {
		return file.getLastUpdated();
	}

	public String getMetadataUri() {
		return file.getMetadataUri();
	}

	public String getMimeType() {
		return file.getMimeType();
	}

	public String getModifiedBy() {
		return file.getModifiedBy();
	}

	@Override
	public String getName() {
		return file.getName();
	}

	public Checksums getOriginalChecksums() {
		return file.getOriginalChecksums();
	}

	@Override
	public String getPath() {
		return file.getPath();
	}

	@Override
	public String getRepo() {
		return file.getRepo();
	}

	@Override
	public long getSize() {
		return file.getSize();
	}

	@Override
	public String getUri() {
		return file.getUri();
	}

	@Override
	public boolean isFolder() {
		return file.isFolder();
	}

	@Override
	public String toString() {
		return "RemoteUploadResult [file=" + file.toString() + "]";
	}

}
