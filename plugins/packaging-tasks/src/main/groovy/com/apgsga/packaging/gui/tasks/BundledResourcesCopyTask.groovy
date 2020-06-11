package com.apgsga.packaging.gui.tasks

import com.apgsga.packaging.extensions.ApgCommonPackageExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class BundledResourcesCopyTask extends DefaultTask { 
	
	// TODO (che, 2.10 ) : Verifiy if and when needed (in general)
	@OutputDirectory
    File getOutputDir() {
		def ex = project.extensions.apgPackage
		return new File("${project.buildDir}/${ex.name}")
	}

	
	@TaskAction
	def taskAction() {
		ApgCommonPackageExtension ex = project.extensions.apgPackage
		def digiflexWebIt21PortNr = ex.getPortNr('digiflex-web-it21')
		// JHE (11.06.2020): Currently the jadas service is not "jadas", this will probably change as soon as digiflex will be 100% ready. Eventually we could provide this name as parameter within ApgCommonPackageExtension
		def jadasPortNr = ex.getPortNr('digiflex-jadas')
		// JHE (10.06.2020): URL such http://chtadg1.apgsga.ch/geoserver/wms are either with "cht" of "chp", basically never starts with "che"
		def targetIndicatorProdOrOther = ex.installTarget.charAt(2).toLowerCase().equals("p") ? "p" : "t"
		project.copy {
			into "${project.buildDir}/${ex.name}"
			from ("${project.buildDir}/packaging/it21-gui") {
				expand(javaOpts:ex.javaOpts,mainProgramm:ex.mainProgramName,pkgName:ex.name,digiflexWebIt21PortNr:"${digiflexWebIt21PortNr}",jadasPortNr:"${jadasPortNr}",
					   target:ex.installTarget.toLowerCase(),targetIndicator:ex.installTarget.charAt(2).toLowerCase(),targetIndicatorProdOrOther:"${targetIndicatorProdOrOther}")
				include '**/*.bat'
				include '**/*.sh'
				include '**/logback.xml'
				include '**/serviceurl.properties'
				include '**/AdGIS.properties'
			}
			from ("${project.buildDir}/packaging/it21-gui") {
				exclude '**/*.bat'
				exclude '**/*.sh'
				exclude '**/logback.xml'
				exclude '**/serviceurl.properties'
				exclude '**/AdGIS.properties'
			}
	
		 
		}
		
	}
}
