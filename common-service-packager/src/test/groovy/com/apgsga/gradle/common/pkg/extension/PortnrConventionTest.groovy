package com.apgsga.gradle.common.pkg.extension

import spock.lang.Specification

class PortnrConventionTest extends Specification {
    def "CalculateDefaultsTest"() {
        given:
            def supportedServices = ["service1", "service2"]
        when:
        def calculate = PortnrConvention.calculate(supportedServices,'CHTI212', 'service2')
        println calculate
        then:
        calculate == "30132"
    }

    def "CalculateOthersSupportedServices"() {
        given:
        def supportedServices = ["service0", "service1", "service2"]
        when:
        def calculate = PortnrConvention.calculate(supportedServices,'CHTI212', 'service2')
        println calculate
        then:
        calculate == "30232"
    }

    def "CalculateOthersSupportedServicesMoreThen9"() {
        given:
        def supportedServices = ["service01", "service02", "service03","service04","service05","service06","service07","service08","service09","service10","service11"]
        when:
        def calculate = PortnrConvention.calculate(supportedServices,'CHTI212', 'service11')
        println calculate
        then:
        calculate == "31032"
    }

    def "CalculateDefaultsEntw"() {
        given:
        def supportedServices = ["service1", "service2"]
        when:
        def calculate = PortnrConvention.calculate(supportedServices,'XXEXX1', 'service2')
        println calculate
        then:
        calculate == "30141"
    }

    def "CalculateEnviromentUndefined"() {
        given:
        def supportedServices = ["service1", "service2"]
        when:
        def calculate = PortnrConvention.calculate(supportedServices,'XXXXX1', 'service2')
        println calculate
        then:
        Exception ex = thrown()
        ex.getMessage() == "Unknown Enviroment Designator: X"
    }

    def "CalculateServiceUndefined"() {
        given:
        def supportedServices = ["service1", "service2"]
        when:
        def calculate = PortnrConvention.calculate(supportedServices,'XXEXX1', 'xxxxxxxxxxxxx')
        println calculate
        then:
        Exception ex = thrown()
        ex.getMessage() == "Service xxxxxxxxxxxxx undefined "
    }


}
