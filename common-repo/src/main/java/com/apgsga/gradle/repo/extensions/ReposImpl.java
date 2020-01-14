package com.apgsga.gradle.repo.extensions;

import com.google.common.collect.Maps;
import org.gradle.api.Project;

import java.util.Map;
// TODO (jhe, che) 10.1.2020 : To discuss and see if using NameDomainObjectContainer not maps easier to Repo Class (DSL and Serialization)
// TODO (jhe, che) 10.1.2020 : see also https://github.com/liquibase/liquibase-gradle-plugin/blob/master/src/main/groovy/org/liquibase/gradle/LiquibaseExtension.groovy
// TODO (jhe, che) 10.1.2020 : see also jetbrains://idea/navigate/reference?project=apg-gradle-plugins&path=com/apgsga/maven/dm/ext/VersionResolution.kt
public class ReposImpl implements  Repos {

    private Project project;

    private Map<RepoType,Repo> repositories = Maps.newHashMap();

    public ReposImpl(Project project) {
        this.project = project;
    }

    public Map<RepoType, Repo> getRepositories() {
        return repositories;
    }

    @Override
    public Repo get(RepoType repoType) {
        return repositories.get(repoType);
    }

    @Override
    public void set(RepoType repoType, Repo repo) {
        this.repositories.put(repoType,repo);
    }

    @Override
    public Repo getRepoFor(String archiveName) {
        return archiveName.toLowerCase().endsWith("rpm") ? get(RepoType.RPM) : archiveName.toLowerCase().endsWith("zip") ? get(RepoType.ZIP) : get(RepoType.MAVEN_RELEASE);
    }

    public void config(RepoType repotype, Map<RepoProperties,String> properties) {
        if(properties.containsKey(RepoProperties.REPO_NAME.name))
            this.repositories.get(repotype).setRepoName(properties.get(RepoProperties.REPO_NAME.name));
        if(properties.containsKey(RepoProperties.REPO_BASE_URL.name))
            this.repositories.get(repotype).setRepoBaseUrl(properties.get(RepoProperties.REPO_BASE_URL.name));
        if(properties.containsKey(RepoProperties.REPO_USER.name))
            this.repositories.get(repotype).setUser(properties.get(RepoProperties.REPO_USER.name));
        if(properties.containsKey(RepoProperties.REPO_PASSWORD.name))
            this.repositories.get(repotype).setPassword(properties.get(RepoProperties.REPO_PASSWORD.name));
    }
}
