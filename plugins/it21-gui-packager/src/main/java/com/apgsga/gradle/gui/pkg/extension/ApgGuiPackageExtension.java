package com.apgsga.gradle.gui.pkg.extension;

import org.gradle.api.Project;

import java.util.Arrays;

public class ApgGuiPackageExtension {
	
	private static final String VERSiON_DEFAULT = "1.0-SNAPSHOT";
	private static final String JAVAOPTS_DEFAULT = "-Xms100m -Xmx2560m -Xincgc -XX:NewRatio=2";
	private static final String RESOURCES_PATH_DEFAULT = "resources";
	private static final String PKGNAME_DEFAULT = "it21gui";
	private String pkgName = PKGNAME_DEFAULT; 
	private String mainProgramm = "com.affichage.it21.ui.runtime.It21GuiRuntimeStarter"; 
	// TODO (che,23.11) : we can also support more Directories here, also for ex with a absolute Path
	private String resourcesPath = RESOURCES_PATH_DEFAULT; 
	private String[] dependencies = new String[] {};
	private String javaOpts = JAVAOPTS_DEFAULT;
	private String version = VERSiON_DEFAULT; 
	
	private final Project project;

	public ApgGuiPackageExtension(Project project) {
		super();
		this.project = project;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getResourcesPath() {
		return resourcesPath;
	}

	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}

	public String[] getDependencies() {
		return dependencies;
	}

	public void setDependencies(String[] dependencies) {
		this.dependencies = dependencies;
	}
	

	public String getMainProgramm() {
		return mainProgramm;
	}

	public void setMainProgramm(String mainProgramm) {
		this.mainProgramm = mainProgramm;
	}
	
	

	public String getJavaOpts() {
		return javaOpts;
	}

	public void setJavaOpts(String javaOpts) {
		this.javaOpts = javaOpts;
	}
	
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	// Standards 
	
	public String getArchiveName() {
		return getPkgName() + "-" + getVersion() +  ".zip"; 
	}

	// Logging 
	public void log() {
		project.getLogger().info(toString());
	}

	@Override
	public String toString() {
		return "ApgGuiPackageExtension [pkgName=" + pkgName + ", mainProgramm=" + mainProgramm + ", resourcesPath="
				+ resourcesPath + ", dependencies=" + Arrays.toString(dependencies) + ", javaOpts=" + javaOpts
				+ ", version=" + version + "]";
	}

	

	
}
