package com.apgsga.gradle.rpm.pkg.tasks
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class CommonPropertyFileMergerTask extends DefaultTask {
	

	protected static void mergePropertyFiles(Project project, filters, from, to, servicePropertiesDir, propertyFile) {
		  
		  def LOGGER = project.getLogger(); 
		  
		  LOGGER.info "Running with config: resources: ${filters}, sourceRootDir ${from} and targetDir ${to}"
		  
		  def resourceFilterParms = filters.split(':')
		  
		  def includeFilters = []
		  def first = true
		  resourceFilterParms.each {
			  includeFilters << '**/' + it.toString() + '/' + propertyFile + '.properties'
		  }
		  
		 LOGGER.info "Running with includeFilters: ${includeFilters}, merging ${propertyFile}.properties"
		  
		
		  def mergeResourcePropertiesFile = new File("${to}/${propertyFile}.properties")
		  mergeResourcePropertiesFile.write "# Generated , TODO write some conclusive information"
		  mergeResourcePropertiesFile.append "\n"
		  mergeResourcePropertiesFile.append "\n"
		  def files =  project.fileTree(dir: "${from}", includes: includeFilters).filter { it.isFile() }.files.forEach {
			   LOGGER.info "Processing ${propertyFile} file ${it.path}"
			  
			   project.file(it.path).withInputStream {
				   mergeResourcePropertiesFile.append it.getText()
				   mergeResourcePropertiesFile.append "\n"
				   mergeResourcePropertiesFile.append "\n"
  
			  }
		  }
		  LOGGER.info("Merge Done with includeFilters: ${includeFilters}, merging ${propertyFile}.properties")
		  LOGGER.info("Merging Service specific properties from : ${servicePropertiesDir}, into ${propertyFile}.properties")
		  files = project.fileTree(dir : "${servicePropertiesDir}", includes: ["**/${propertyFile}.properties"] ).filter { it.isFile() }.files.forEach {
			   LOGGER.info "Processing ${propertyFile} file ${it.path}"
			  
			   project.file(it.path).withInputStream {
				   mergeResourcePropertiesFile.append it.getText()
				   mergeResourcePropertiesFile.append "\n"
				   mergeResourcePropertiesFile.append "\n"
  
			  }
		  }
		  LOGGER.info("Merging Service specific properties done.")
	  }
}

class ResourceFileMergerTask extends CommonPropertyFileMergerTask {
	
	@OutputDirectory
	public File getOutputDir() { return new File("${project.buildDir}/template/app/conf/ops") }

	@TaskAction
	def taskAction() {
		def resourceFilters = project.extensions.apgRpmPackage.resourceFilters
		def servicePropertiesDir = project.extensions.apgRpmPackage.servicePropertiesDir
		mergePropertyFiles(project, resourceFilters, "${project.buildDir}/packageing/resources","${project.buildDir}/template/app/conf/ops","${project.projectDir}/${servicePropertiesDir}",'resource')
	}
}
class AppConfigFileMergerTask extends CommonPropertyFileMergerTask {
	
	@OutputDirectory
	public File getOutputDir() { return new File("${project.buildDir}/template/app/conf/app") }

	@TaskAction
	def taskAction() {
		def appConfigFilters = project.extensions.apgRpmPackage.appConfigFilters
		def servicePropertiesDir = project.extensions.apgRpmPackage.servicePropertiesDir
		mergePropertyFiles(project, appConfigFilters,"${project.buildDir}/packageing/appconfigs","${project.buildDir}/template/app/conf/app","${project.projectDir}/${servicePropertiesDir}",'appconfig')
	}
}
