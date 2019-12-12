package com.apgsga.gradle.repo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;

public class RepoNamesPersistenceUtil {

    public static RepoNamesBean loadRepoNames(Resource jsonFile) {
        RepoNamesBean repoNamesBean = null;
        ObjectMapper mapper = new ObjectMapper(); //.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            repoNamesBean = mapper.readValue(jsonFile.getInputStream(), RepoNamesBean.class);
            Assert.notNull(repoNamesBean, "Problem while reading " + jsonFile.getFilename() + ". Result of deserialization was null!");
        } catch (IOException e) {
            throw new RuntimeException("Unable to read repos information from " + jsonFile.getFilename() + ". Original exception was : " + e.getMessage());
        }
        return repoNamesBean;
    }
}