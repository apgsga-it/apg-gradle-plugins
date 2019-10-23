package com.apgsga.gradle.gui.pkg.extension;

import java.util.Arrays;

import org.gradle.api.Project;

public class ApgGuiPackageExtension {
	
	

	private static final String RESOURCES_PATH_DEFAULT = "resources";
	private static final String PKGNAME_DEFAULT = "it21gui";
	private String pkgName = PKGNAME_DEFAULT; 
	private String mainProgramm = "com.affichage.it21.ui.runtime.It21GuiRuntimeStarter"; 
	private String resourcesPath = RESOURCES_PATH_DEFAULT; 
	private String[] dependencies = new String[] {};
	
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

	// Logging 
	public void log() {
		project.getLogger().info(toString());
	}

	@Override
	public String toString() {
		return "ApgGuiPackageExtension [pkgName=" + pkgName + ", mainProgramm=" + mainProgramm + ", resourcesPath="
				+ resourcesPath + ", dependencies=" + Arrays.toString(dependencies) + "]";
	}


}
