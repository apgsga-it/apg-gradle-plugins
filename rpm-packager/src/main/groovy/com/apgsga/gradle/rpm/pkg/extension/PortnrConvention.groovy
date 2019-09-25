package com.apgsga.gradle.rpm.pkg.extension

// TODO (che, 25.9 ) : verify and expand to support all Target Types (production etc)
class PortnrConvention {
	
	def static String calculate(def servicesNames, def target , def serviceName) {
		println "Portnummer calculate Parameters, target: ${target} , serviceName: ${serviceName}"
		def servicePart =  servicesNames.indexOf("${serviceName}".trim())
		if (servicePart < 0) {
			throw new Exception("Service ${serviceName} undefined ")
		}
		def envPart
		def envDesignator = target.substring(2,3)
		switch (envDesignator) {
			case "T":
				envPart = "3"
				break
			case "E":
				envPart = "4"
				break
			default:
				envPart = "4"
				break
		}
		def dbPart = target.reverse().take(1)
		def calculatedPortNr = "30${servicePart}${envPart}${dbPart}"
		println "calculatedPortNr : ${calculatedPortNr}"
		calculatedPortNr

	}
}
