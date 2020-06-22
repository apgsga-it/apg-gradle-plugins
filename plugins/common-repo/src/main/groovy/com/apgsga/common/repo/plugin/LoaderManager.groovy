package com.apgsga.common.repo.plugin

import org.apache.maven.model.InputLocation
import org.apache.maven.model.Profile
import org.apache.maven.model.building.ModelProblem
import org.apache.maven.model.building.ModelProblemCollector
import org.apache.maven.model.path.DefaultPathTranslator
import org.apache.maven.model.profile.DefaultProfileActivationContext
import org.apache.maven.model.profile.DefaultProfileSelector
import org.apache.maven.model.profile.activation.*
import org.apache.maven.settings.Server
import org.apache.maven.settings.Settings
import org.apache.maven.settings.SettingsUtils
import org.apache.maven.settings.building.SettingsBuildingException
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.logging.Logger
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.publish.PublishingExtension
import org.springframework.util.Assert

class LoaderManager {

    public static final String APG_COMMON_REPO_MAVEN_FILE_PATH = "apg.common.repo.maven.file.path"

    public static final String APG_COMMON_REPO_GRADLE_MAVEN_ACTIVE_PROFILE = "apg.common.repo.gradle.maven.active.profile"

    public static final String APG_COMMON_REPO_GRADLE_LOCAL_REPO_FROM_MAVEN = "apg.common.repo.gradle.local.repo.from.maven"

    private Project project

    private Settings settings

    private String userSettingsFileName

    private String activeProfile

    private boolean gradleLocalRepoFromMaven = true

    private boolean exportGradleProps = true

    Logger log

    LoaderManager(Project project) {
        this.project = project;
        // TODO JHE: do we want to have default values here?
        this.userSettingsFileName = project.property(APG_COMMON_REPO_MAVEN_FILE_PATH)
        Assert.notNull(this.userSettingsFileName, "Missing ${APG_COMMON_REPO_MAVEN_FILE_PATH} property")
        this.activeProfile = project.property(APG_COMMON_REPO_GRADLE_MAVEN_ACTIVE_PROFILE)
        Assert.notNull(this.activeProfile, "Missing ${APG_COMMON_REPO_GRADLE_MAVEN_ACTIVE_PROFILE} property")
        gradleLocalRepoFromMaven = doCreateGradleLocalFromMaven() ? true : false
        log = project.logger
    }

    private boolean doCreateGradleLocalFromMaven() {
        if(!project.properties.containsKey(APG_COMMON_REPO_GRADLE_LOCAL_REPO_FROM_MAVEN)) {
            return true
        }
        return project.property(APG_COMMON_REPO_GRADLE_LOCAL_REPO_FROM_MAVEN).equals("true")
    }

    void load() {
        loadSettings()
        if(gradleLocalRepoFromMaven) {configureLocalRepository()}
        activateProfiles()
    }

    private void configureLocalRepository() {
        String localRepoUrl = settings.localRepository

        project.repositories.maven {m ->
            log.info("Applying a local gradle maven Repo at following location ${localRepoUrl}")
            m.setUrl(localRepoUrl)
            m.artifactUrls(localRepoUrl)
            m.setName("local")
        }

        project.extensions.findByType(PublishingExtension)?.repositories?.all {gradleRepo ->
            if (gradleRepo instanceof MavenArtifactRepository) {
                if(gradleRepo.name.equalsIgnoreCase("local")) {
                    log.info("Applying a local publish Repo.")
                    gradleRepo.url = localRepoUrl
                }
            }
        }
    }

    private void loadSettings() {
        LocalMavenSettingsLoader settingsLoader = new LocalMavenSettingsLoader(userSettingsFileName)
        try {
            settings = settingsLoader.loadSettings()
        } catch (SettingsBuildingException e) {
            e.printStackTrace()
            println e.getMessage()
            throw new RuntimeException('Unable to read local Maven settings.', e)
        }
    }

    private List<Profile> getProfiles() {
        DefaultProfileSelector profileSelector = new DefaultProfileSelector()
        DefaultProfileActivationContext activationContext = new DefaultProfileActivationContext()
        List<ProfileActivator> profileActivators = [new JdkVersionProfileActivator(), new OperatingSystemProfileActivator(),
                                                    new PropertyProfileActivator(), new FileProfileActivator().setPathTranslator(new DefaultPathTranslator())]
        profileActivators.each { profileSelector.addProfileActivator(it) }

        activationContext.setActiveProfileIds([activeProfile])
        activationContext.setProjectDirectory(project.projectDir)
        activationContext.setSystemProperties(System.getProperties())
        if (exportGradleProps) {
            activationContext.setUserProperties(project.properties.collectEntries { key, value -> [key, value.toString()] } as Map<String, String>)
        }

        List<Profile> profiles = profileSelector.getActiveProfiles(settings.profiles.collect { return SettingsUtils.convertFromSettingsProfile(it) },
                activationContext, new ModelProblemCollector() {
            @Override
            void add(ModelProblem.Severity severity, String s, InputLocation inputLocation, Exception e) {

            }
        })

        return profiles
    }

    private void loadPublishExtension(Profile profile) {
        project.extensions.findByType(PublishingExtension)?.repositories?.all {gradleRepo ->
            log.info("Publish Repository '${gradleRepo.name}' found.")
            if (gradleRepo instanceof MavenArtifactRepository) {
                profile.repositories.each {mavenConfigRepo ->
                    if(mavenConfigRepo.name == gradleRepo.name){
                        log.info("'${gradleRepo.name}' name match a maven repo -> '${mavenConfigRepo.name}'")
                        gradleRepo.url = mavenConfigRepo.url
                        if(!gradleRepo.name.equalsIgnoreCase("local")) {
                            settings.servers.each { server ->
                                if (mavenConfigRepo.id == server.id) {
                                    log.info("Credentials found for Repo '${gradleRepo.name}'. Credentials Id is '${server.id}'")
                                    addCredentials(server, gradleRepo as MavenArtifactRepository)
                                }
                            }
                        }
                    }
                }
            }
            log.info("Publish Repository '${gradleRepo.name}' -> DONE")
        }
    }

    private void loadDependenciesRepositories(Profile profile) {
        log.info("Dependencies Repositores confiuration will be applied.")
        profile.repositories.each {mavenConfigRepo ->
            log.info("Dealing with Repo called '${mavenConfigRepo.name}'")
            project.repositories.maven {m ->
                log.info("Applying a gradle maven Repo with following properties: url -> ${mavenConfigRepo.url}, artifactsUrl -> ${mavenConfigRepo.url}, name -> ${mavenConfigRepo.name}")
                m.setUrl(mavenConfigRepo.url)
                m.artifactUrls(mavenConfigRepo.url)
                m.setName(mavenConfigRepo.name)
                if(!mavenConfigRepo.name.equalsIgnoreCase("local")) {
                    settings.servers.each { server ->
                        if (mavenConfigRepo.id == server.id) {
                            log.info("Credentials found for Repo '${mavenConfigRepo.name}'. Credentials Id is '${server.id}'")
                            addCredentials(server, m as MavenArtifactRepository)
                        }
                    }
                }
            }
        }
    }

    private void activateProfiles() {

        Logger log = project.logger


        getProfiles().each { profile ->

            if(activeProfile.equalsIgnoreCase(profile.id)) {

                log.info("Parsing profile '${profile.id}'")

                // TODO JHE: do we really need this loop ?!?
                for (Map.Entry entry : profile.properties) {
                    project.extensions.getByType(ExtraPropertiesExtension).set(entry.key.toString(), entry.value.toString())
                }

                loadPublishExtension(profile)
                loadDependenciesRepositories(profile)

                log.info("${profile.id} Profile has been parsed successfuly!")
            }
        }

    }

    private static addCredentials(Server server, MavenArtifactRepository repo) {
        if (server?.username != null && server?.password != null) {
            repo.credentials {
                it.username = server.username
                it.password = server.password
            }
        }
    }

}
