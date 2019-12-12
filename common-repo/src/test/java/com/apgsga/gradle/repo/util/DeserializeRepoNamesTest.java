package com.apgsga.gradle.repo.util;

import com.apgsga.gradle.repo.extensions.RepoType;
import com.apgsga.gradle.repo.extensions.Repos;
import com.apgsga.gradle.repo.extensions.ReposImpl;
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
    public void readRepoNamesContent() throws IOException {
        //TODO JHE: correctly write test ..
        Resource repoNamesTest = new ClassPathResource("repoNamesTest.json");
        ReposImpl r = new ObjectMapper().readerFor(ReposImpl.class).readValue(repoNamesTest.getInputStream());
        System.out.println(r.getRepoUserName());
        System.out.println(r.getRepos());
    }
}