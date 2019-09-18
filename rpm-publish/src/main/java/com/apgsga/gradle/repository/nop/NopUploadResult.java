package com.apgsga.gradle.repository.nop;

import java.util.Date;

import com.apgsga.gradle.repository.UploadResult;

public class NopUploadResult implements UploadResult {
	
	public static UploadResult NOP = new NopUploadResult(); 

	private NopUploadResult() {

	}

	@Override
	public Date getCreated() {
		return new Date();
	}

	@Override
	public String getCreatedBy() {
		return "";
	}

	@Override
	public String getDownloadUri() {
		return "";
	}

	@Override
	public Date getLastModified() {
		return new Date();
	}

	@Override
	public Date getLastUpdated() {
		return new Date();
	}

	@Override
	public String getName() {
		return ""; 
	}

	@Override
	public String getPath() {
		return ""; 
	}

	@Override
	public String getRepo() {
		return ""; 
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public String getUri() {
		return ""; 
	}

	@Override
	public boolean isFolder() {
		return false;
	}

	@Override
	public String toString() {
		return "NopUploadResult []";
	}
	
	

}
