package com.apgsga.gradle.repository.nop;

import java.io.File;

import com.apgsga.gradle.repository.UploadRepository;
import com.apgsga.gradle.repository.UploadResult;

public class NopUploadRepository implements UploadRepository {
	
	public static UploadRepository  NOP = new NopUploadRepository();

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public UploadResult upload(String fileName, File fileToUpload) {
		return NopUploadResult.NOP; 
	}

	

}
