package com.apgsga.gradle.rpm.pkg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class BinariesCopyTask extends DefaultTask { 
	
	// TODO (che, 2.10 ) : Verifiy if and when needed (in general)
	@OutputDirectory
	public File getOutputDir() { return new File("${project.buildDir}/app-pkg/app/lib") }

	
	@TaskAction
	def taskAction() {
		def ex = project.extensions.apgRpmPackage
		// TODO (che, 1.10.19) : make excludes configurable via extension
		project.configurations {
			serviceRuntime
			serviceRuntime.exclude group: 'log4j', module: 'log4j'
			serviceRuntime.exclude group: 'org.neo4j' , module: 'neo4j-ogm'
			serviceRuntime.exclude group: 'org.neo4j' , module: 'neo4j-ogm'
			serviceRuntime.exclude group: 'org.codehaus.groovy' , module: 'groovy-all'
		}

		project.dependencies {  
			ex.dependencies.each {  
				serviceRuntime it
			}
		}
		project.copy {
			into "${project.buildDir}/app-pkg/app/lib"
			from project.configurations.serviceRuntime
		}
		
	}
}
