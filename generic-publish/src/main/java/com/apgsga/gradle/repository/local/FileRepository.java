package com.apgsga.gradle.repository.local;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;


import com.apgsga.gradle.repository.UploadRepository;
import com.apgsga.gradle.repository.UploadResult;
import com.google.common.base.Preconditions;

public class FileRepository implements UploadRepository {

	private final File repoDir;

	public FileRepository(File repoDir) {
		super();
		this.repoDir = repoDir;
	}

	@Override
	public boolean exists() {
		return repoDir.exists();
	}

	@Override
	public UploadResult upload(String fileName, File fileToUpload) {
		Preconditions.checkState(fileToUpload.exists(), "File to Upload does'nt exist");
		Preconditions.checkState(fileToUpload.isFile(), "File to Upload is'nt File");
		Preconditions.checkState(fileToUpload.canRead(), "File to Upload is'nt readable");
		File targetFile = new File(repoDir, fileName);
		System.out.println("Uplaoding File: " + fileName + ", to: " + repoDir.getAbsolutePath()); 
		try {
			FileUtils.copyFile(fileToUpload, targetFile);
			System.out.println("Uplaoding File: " + fileName + ", done"); 
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return new FileUploadResult(targetFile);
	}


	public String getName() {
		return repoDir.getName();
	}

	public String getParent() {
		return repoDir.getParent();
	}

	public File getParentFile() {
		return repoDir.getParentFile();
	}

	public String getPath() {
		return repoDir.getPath();
	}

	public boolean isAbsolute() {
		return repoDir.isAbsolute();
	}

	public String getAbsolutePath() {
		return repoDir.getAbsolutePath();
	}

	public File getAbsoluteFile() {
		return repoDir.getAbsoluteFile();
	}

	public String getCanonicalPath() throws IOException {
		return repoDir.getCanonicalPath();
	}

	public File getCanonicalFile() throws IOException {
		return repoDir.getCanonicalFile();
	}

	public URL toURL() throws MalformedURLException {
		return repoDir.toURL();
	}

	public URI toURI() {
		return repoDir.toURI();
	}

	public boolean canRead() {
		return repoDir.canRead();
	}

	public boolean canWrite() {
		return repoDir.canWrite();
	}

	public boolean isDirectory() {
		return repoDir.isDirectory();
	}

	public boolean isFile() {
		return repoDir.isFile();
	}

	public boolean isHidden() {
		return repoDir.isHidden();
	}

	public long lastModified() {
		return repoDir.lastModified();
	}

	public long length() {
		return repoDir.length();
	}

	public boolean createNewFile() throws IOException {
		return repoDir.createNewFile();
	}

	public boolean delete() {
		return repoDir.delete();
	}

	public void deleteOnExit() {
		repoDir.deleteOnExit();
	}

	public String[] list() {
		return repoDir.list();
	}

	public String[] list(FilenameFilter filter) {
		return repoDir.list(filter);
	}

	public File[] listFiles() {
		return repoDir.listFiles();
	}

	public File[] listFiles(FilenameFilter filter) {
		return repoDir.listFiles(filter);
	}

	public File[] listFiles(FileFilter filter) {
		return repoDir.listFiles(filter);
	}

	public boolean mkdir() {
		return repoDir.mkdir();
	}

	public boolean mkdirs() {
		return repoDir.mkdirs();
	}

	public boolean renameTo(File dest) {
		return repoDir.renameTo(dest);
	}

	public boolean setLastModified(long time) {
		return repoDir.setLastModified(time);
	}

	public boolean setReadOnly() {
		return repoDir.setReadOnly();
	}

	public boolean setWritable(boolean writable, boolean ownerOnly) {
		return repoDir.setWritable(writable, ownerOnly);
	}

	public boolean setWritable(boolean writable) {
		return repoDir.setWritable(writable);
	}

	public boolean setReadable(boolean readable, boolean ownerOnly) {
		return repoDir.setReadable(readable, ownerOnly);
	}

	public boolean setReadable(boolean readable) {
		return repoDir.setReadable(readable);
	}

	public boolean setExecutable(boolean executable, boolean ownerOnly) {
		return repoDir.setExecutable(executable, ownerOnly);
	}

	public boolean setExecutable(boolean executable) {
		return repoDir.setExecutable(executable);
	}

	public boolean canExecute() {
		return repoDir.canExecute();
	}

	public long getTotalSpace() {
		return repoDir.getTotalSpace();
	}

	public long getFreeSpace() {
		return repoDir.getFreeSpace();
	}

	public long getUsableSpace() {
		return repoDir.getUsableSpace();
	}

	public int compareTo(File pathname) {
		return repoDir.compareTo(pathname);
	}

	public boolean equals(Object obj) {
		return repoDir.equals(obj);
	}

	public int hashCode() {
		return repoDir.hashCode();
	}

	public String toString() {
		return repoDir.toString();
	}

	public Path toPath() {
		return repoDir.toPath();
	}

}
