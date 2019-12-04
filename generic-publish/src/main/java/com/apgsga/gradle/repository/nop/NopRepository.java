package com.apgsga.gradle.repository.nop;

import com.apgsga.gradle.repository.Repository;
import com.apgsga.gradle.repository.Result;

import java.io.File;
import java.io.InputStream;

public class NopRepository implements Repository {
	
	public static Repository NOP = new NopRepository();

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public Result upload(String fileName, File fileToUpload) {
		return NopResult.NOP;
	}

	@Override
	public InputStream download(String path) {
		return null;
	}


}
