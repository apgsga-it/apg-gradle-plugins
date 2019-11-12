package com.apgsga.gradle.repository.nop;

import java.io.File;
import java.io.InputStream;

import com.apgsga.gradle.repository.Repository;
import com.apgsga.gradle.repository.Result;

public class NopRepository implements Repository {
	
	static Repository NOP = new NopRepository();

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
