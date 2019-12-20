package com.apgsga.gradle.repo.util;

import com.apgsga.gradle.repo.extensions.RepoType;
import com.apgsga.gradle.repo.extensions.Repos;
import com.apgsga.gradle.repo.extensions.ReposImpl;
import com.apgsga.gradle.repo.plugin.RepoNamesBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public class DeserializeRepoNamesTest {

    @Test
    public void testReadRepoNamesContent() throws IOException {
        Resource repoNamesTest = new ClassPathResource("repoNamesTest.json");
        RepoNamesBean r = new ObjectMapper().readerFor(RepoNamesBean.class).readValue(repoNamesTest.getInputStream());
        Assert.assertEquals("Not all repos correctly loaded", 4, r.repos.size());
        Assert.assertEquals("user name not correctly loaded", "gradledev-tests-user", r.repoUserName);
        Assert.assertEquals("user password not correctly loaded", "gradledev-tests-user", r.repoUserPwd);
        Assert.assertEquals("repo URL not correctly loaded", "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga", r.repoBaseUrl);
        r.repos.forEach(m -> {
            // Each Map always had exactly one key and one value
            Assert.assertEquals("repos Map cannot have more than one key", 1, m.keySet().size());
            Assert.assertEquals("repos Map cannot have more than one value", 1, m.values().size());
            RepoType rt = (RepoType) m.keySet().toArray()[0];
            switch (rt) {
                case ZIP:
                    Assert.assertEquals(rt.asString() + "repo not correctly loaded", "release-functionaltest", m.values().toArray()[0]);
                    break;
                case RPM:
                    Assert.assertEquals(rt.asString() + "repo not correctly loaded", "rpm-functionaltest", m.values().toArray()[0]);
                    break;
                case MAVEN_SNAPSHOT:
                    Assert.assertEquals(rt.asString() + "repo not correctly loaded", "snapshot-functionaltest", m.values().toArray()[0]);
                    break;
                case MAVEN_RELEASE:
                    Assert.assertEquals(rt.asString() + "repo not correctly loaded", "release-functionaltest", m.values().toArray()[0]);
                    break;
                default:
                    Assert.fail(rt.asString() + " has been loaded, but was not in repoNamesTest.json");
            }
        });
    }
}