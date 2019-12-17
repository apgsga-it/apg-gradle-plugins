package com.apgsga.gradle.common.pkg.plugin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SupportedServicesBean {

    @JsonProperty
    public List<String> supportedServices;
}
