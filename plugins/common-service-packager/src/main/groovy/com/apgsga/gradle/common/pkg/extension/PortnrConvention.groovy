package com.apgsga.gradle.common.pkg.extension

// TODO (che, 25.9 ) : verify and expand to support all Target Types (production etc)
class PortnrConvention {
	
	static String calculate(def servicesNames, def target , def serviceName) {
		println "Portnummer calculate Parameters, target: ${target} , serviceName: ${serviceName}"
		println "For serviceNames: ${servicesNames}"
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
				throw new Exception("Unknown Enviroment Designator: ${envDesignator}")
				break
		}
		def dbPart = target.reverse().take(1)
		def calculatedPortNr
		if (servicePart > 9 ) {
			calculatedPortNr = "3${servicePart}${envPart}${dbPart}"

		} else {
			calculatedPortNr = "30${servicePart}${envPart}${dbPart}"
		}
		println "calculatedPortNr : ${calculatedPortNr}"
		calculatedPortNr

	}
}
