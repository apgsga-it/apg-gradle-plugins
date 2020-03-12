package com.apgsga.gradle.repository.artifactory;

import com.apgsga.gradle.repository.Repository;
import com.apgsga.gradle.repository.Result;
import org.jfrog.artifactory.client.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RemoteRepository implements Repository {

	private final RepositoryHandle repositoryHandle;

	RemoteRepository(RepositoryHandle repositoryHandle) {
		super();
		this.repositoryHandle = repositoryHandle;
	}

	@Override
	public boolean exists() {
		return repositoryHandle.exists();
	}

	@Override
	public Result upload(String fileName, File fileToUpload) {
		UploadableArtifact uploadable = repositoryHandle.upload(fileName, fileToUpload);
		uploadable.withListener(new UploadListener() {

			double lastprz = 0;

			@Override
			public void uploadProgress(long actuel, long total) {
				double prz = (100 * actuel) / total;
				if ((lastprz + 10.0) <= prz) {
					System.out.println(" Upload Progress ->  " + fileName + " : " + prz + "%");
					lastprz = prz;
				}
			}
		});
		return new RemoteResult(uploadable.doUpload());
	}

	@Override
	public InputStream download(String relativePath)  {
		DownloadableArtifact artifact = repositoryHandle.download(relativePath);
		try {
			return artifact.doDownload();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public String delete(String path) {
		return repositoryHandle.delete(path);
	}

	public ItemHandle file(String arg0) {
		return repositoryHandle.file(arg0);
	}

	public ItemHandle folder(String path) {
		return repositoryHandle.folder(path);
	}

	public boolean isFolder(String path) throws IOException {
		return repositoryHandle.isFolder(path);
	}

	public UploadableArtifact upload(String path, InputStream inputStream) {
		return repositoryHandle.upload(path, inputStream);
	}

}
