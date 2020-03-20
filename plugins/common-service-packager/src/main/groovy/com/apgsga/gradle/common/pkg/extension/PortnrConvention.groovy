package com.apgsga.gradle.common.pkg.extension

import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension.UnknownPropertyException


class PortnrConvention {

    static def calculate(Project proj , def target, def service) {
		try {
			def portNrClosureMap = proj.extensions.extraProperties.get("portNr")
			return portNrClosureMap.calc.call(target,service)
		} catch (UnknownPropertyException ignored) {
			proj.logger.warn("Port Nr Calculation Closure not found, providing default")
			return "XXXXX"
		}
	}

	static def list(Project proj) {
		try {
			def portNrClosureMap = proj.extensions.extraProperties.get("portNr")
			portNrClosureMap.list.call()
		} catch (UnknownPropertyException ignored) {
			proj.logger.warn("Port Nr List Calculation Closure not found, providing default")
		}
	}
}
