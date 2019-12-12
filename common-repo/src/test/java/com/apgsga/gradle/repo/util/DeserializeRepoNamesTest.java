package com.apgsga.gradle.repo.util;

import com.apgsga.gradle.repo.extensions.RepoType;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

public class DeserializeRepoNamesTest {

    @Test
    public void readRepoNamesContent() {
        Resource repoNamesTest = new ClassPathResource("repoNamesTest.json");
        Assert.assertNotNull("repoNamesTest.json File doesn't exist", repoNamesTest.exists());
        RepoNamesPersistenceUtil persistenceUtil = new RepoNamesPersistenceUtil();
        RepoNamesBean repoTypesBean = persistenceUtil.loadRepoNames(repoNamesTest);
        Assert.assertEquals("Number of RepoType is wrong", 4, repoTypesBean.getRepos().size());
        Assert.assertEquals("repoUserName is wrong", "gradledev-tests-user", repoTypesBean.getRepoUserName());
        Assert.assertEquals("repoUserPwd is wrong", "gradledev-tests-user", repoTypesBean.getRepoUserPwd());
        Assert.assertEquals("repoBaseUrl is wrong", "https://artifactory4t4apgsga.jfrog.io/artifactory4t4apgsga", repoTypesBean.getRepoBaseUrl());
        List<RepoType> expectedRepoType = Lists.newArrayList(RepoType.ZIP,RepoType.RPM,RepoType.MAVEN_SNAPSHOT,RepoType.MAVEN_RELEASE);
        List<String> expectedRepoName = Lists.newArrayList("release-functionaltest","rpm-functionaltest","snapshot-functionaltest");
        repoTypesBean.getRepos().forEach(rt -> {
            Assert.assertTrue(rt.keySet().toArray()[0] + " not in the list of expected repo type : " + expectedRepoType, expectedRepoType.containsAll(rt.keySet()));
            Assert.assertTrue(rt.values().toArray()[0] + " not in the list of expected repo names : " + expectedRepoName, expectedRepoName.containsAll(rt.values()));
        });
    }
}