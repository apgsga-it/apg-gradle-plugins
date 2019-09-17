package com.apgsga.gradle.repository.artifactory;

import java.io.File;
import java.io.InputStream;

import org.jfrog.artifactory.client.DownloadableArtifact;
import org.jfrog.artifactory.client.ItemHandle;
import org.jfrog.artifactory.client.RepositoryHandle;
import org.jfrog.artifactory.client.UploadListener;
import org.jfrog.artifactory.client.UploadableArtifact;

import com.apgsga.gradle.repository.UploadRepository;
import com.apgsga.gradle.repository.UploadResult;

public class RemoteRepository implements UploadRepository {
		
	private final RepositoryHandle repositoryHandle;

	public RemoteRepository(RepositoryHandle repositoryHandle) {
		super();
		this.repositoryHandle = repositoryHandle;
	}

	@Override
	public boolean exists() {
		return repositoryHandle.exists();
	}

	@Override
	public UploadResult upload(String fileName, File fileToUpload) {
		UploadableArtifact uploadable = repositoryHandle.upload(fileName, fileToUpload);
		uploadable.withListener(new UploadListener() {
			
			double lastprz = 0;
			
			@Override
			public void uploadProgress(long actuel, long total) {
				double prz = (100 * actuel) / total;
				if ((lastprz + 10.0) < prz ) {
					System.out.println("Progress: " + prz + "%");
					lastprz = prz;
				}
			}
		});
		return new RemoteUploadResult(uploadable.doUpload());
	}

	public String delete(String path) {
		return repositoryHandle.delete(path);
	}

	public DownloadableArtifact download(String path) {
		return repositoryHandle.download(path);
	}

	public ItemHandle file(String arg0) {
		return repositoryHandle.file(arg0);
	}

	public ItemHandle folder(String path) {
		return repositoryHandle.folder(path);
	}

	public boolean isFolder(String path) {
		return repositoryHandle.isFolder(path);
	}

	public UploadableArtifact upload(String path, InputStream inputStream) {
		return repositoryHandle.upload(path, inputStream);
	}
	
	
	



}
