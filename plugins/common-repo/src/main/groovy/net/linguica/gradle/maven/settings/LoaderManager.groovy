package net.linguica.gradle.maven.settings


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

public class LoaderManager {

    private Project project;

    private Settings settings

    public LoaderManager(Project project) {
        this.project = project;
    }

    public void load() {
        MavenSettingsPluginExtension mavenExtension = project.extensions.findByName(MavenSettingsPlugin.MAVEN_SETTINGS_EXTENSION_NAME);

//        project.afterEvaluate {
            // TODO JHE: We do not need to pass the project as parameter here.
            loadSettings(mavenExtension)
            activateProfiles(project, mavenExtension)
//        }
    }

    private void loadSettings(MavenSettingsPluginExtension extension) {
        LocalMavenSettingsLoader settingsLoader = new LocalMavenSettingsLoader(extension)
        try {
            settings = settingsLoader.loadSettings()
        } catch (SettingsBuildingException e) {
            e.printStackTrace()
            println e.getMessage()
            //throw new GradleScriptException('Unable to read local Maven settings.', e)
            throw new RuntimeException('Unable to read local Maven settings.', e)
        }
    }

    private void activateProfiles(Project project, MavenSettingsPluginExtension extension) {

        Logger log = project.logger

        DefaultProfileSelector profileSelector = new DefaultProfileSelector()
        DefaultProfileActivationContext activationContext = new DefaultProfileActivationContext()
        List<ProfileActivator> profileActivators = [new JdkVersionProfileActivator(), new OperatingSystemProfileActivator(),
                                                    new PropertyProfileActivator(), new FileProfileActivator().setPathTranslator(new DefaultPathTranslator())]
        profileActivators.each { profileSelector.addProfileActivator(it) }

        activationContext.setActiveProfileIds(extension.activeProfiles.toList() + settings.activeProfiles)
        activationContext.setProjectDirectory(project.projectDir)
        activationContext.setSystemProperties(System.getProperties())
        if (extension.exportGradleProps) {
            activationContext.setUserProperties(project.properties.collectEntries { key, value -> [key, value.toString()] } as Map<String, String>)
        }

        List<Profile> profiles = profileSelector.getActiveProfiles(settings.profiles.collect { return SettingsUtils.convertFromSettingsProfile(it) },
                activationContext, new ModelProblemCollector() {
            @Override
            void add(ModelProblem.Severity severity, String s, InputLocation inputLocation, Exception e) {

            }
        })

        log.info("Maven Profile will be parse to apply Repositories configuration")
        profiles.each { profile ->

            log.info("Parsing profile '${profile.id}'")

            // TODO JHE: do we really need this loop ?!?
            for (Map.Entry entry: profile.properties) {
                project.extensions.getByType(ExtraPropertiesExtension).set(entry.key.toString(), entry.value.toString())
            }

            // Publish extension
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

            // apply "standard" repository configuration
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
        log.info("Maven Profile have been parsed successfuly!")
    }

//    private void registerMirrors(Project project) {
//        Mirror globalMirror = settings.mirrors.find { it.mirrorOf.split(',').contains('*') }
//        if (globalMirror != null) {
//            project.logger.info "Found global mirror in settings.xml. Replacing Maven repositories with mirror " +
//                    "located at ${globalMirror.url}"
//            createMirrorRepository(project, globalMirror)
//            return
//        }
//
//        Mirror externalMirror = settings.mirrors.find { it.mirrorOf.split(',').contains('external:*') }
//        if (externalMirror != null) {
//            project.logger.info "Found external mirror in settings.xml. Replacing non-local Maven repositories " +
//                    "with mirror located at ${externalMirror.url}"
//            createMirrorRepository(project, externalMirror) { MavenArtifactRepository repo ->
//                InetAddress host = InetAddress.getByName(repo.url.host)
//                // only match repositories not on localhost and not file based
//                repo.url.scheme != 'file' && !(host.anyLocalAddress || host.isLoopbackAddress() || NetworkInterface.getByInetAddress(host) != null)
//            }
//            return
//        }
//
//        Mirror centralMirror = settings.mirrors.find { it.mirrorOf.split(',').contains('central') }
//        if (centralMirror != null) {
//            project.logger.info "Found central mirror in settings.xml. Replacing Maven Central repository with " +
//                    "mirror located at ${centralMirror.url}"
//            createMirrorRepository(project, centralMirror) { MavenArtifactRepository repo ->
//                ArtifactRepositoryContainer.MAVEN_CENTRAL_URL.startsWith(repo.url.toString())
//            }
//        }
//    }

//    private void applyRepoCredentials(@Nullable RepositoryHandler repositories) {
//        repositories?.all { repo ->
//            if (repo instanceof MavenArtifactRepository) {
//                settings.servers.each { server ->
//                    if (repo.name == server.id) {
//                        addCredentials(server, repo as MavenArtifactRepository)
//                    }
//                }
//            }
//        }
//    }

//    private void createMirrorRepository(Project project, Mirror mirror) {
//        createMirrorRepository(project, mirror) { true }
//    }

//    private void createMirrorRepository(Project project, Mirror mirror, Closure predicate) {
//        boolean mirrorFound = false
//        List<String> excludedRepositoryNames = mirror.mirrorOf.split(',').findAll { it.startsWith("!") }.collect { it.substring(1) }
//        project.repositories.all { repo ->
//            if (repo instanceof MavenArtifactRepository && repo.name != ArtifactRepositoryContainer.DEFAULT_MAVEN_LOCAL_REPO_NAME
//                    && repo.url != URI.create(mirror.url) && predicate(repo) && !excludedRepositoryNames.contains(repo.getName())) {
//                project.repositories.remove(repo)
//                mirrorFound = true
//            }
//        }
//
//        if (mirrorFound) {
//            Server server = settings.getServer(mirror.id)
//            project.repositories.maven { MavenArtifactRepository repo ->
//                repo.name = mirror.name ?: mirror.id
//                repo.url = mirror.url
//                addCredentials(server, repo)
//            }
//        }
//    }

    private static addCredentials(Server server, MavenArtifactRepository repo) {
        if (server?.username != null && server?.password != null) {
            repo.credentials {
                it.username = server.username
                it.password = server.password
            }
        }
    }

//    private void initRepoExtension(Repos repos) {
//        repos.set(RepoType.LOCAL, getLocalRepo())
//        repos.set(RepoType.MAVEN, getMavenRepo())
//    }
//
//    private Repo getLocalRepo() {
//        return new ApgRepo(settings.getLocalRepository(), RepoProperties.REPO_NAME.name(),"","")
//    }
//
//    private Repo getMavenRepo() {
//        def p = project.extensions.getByType(ExtraPropertiesExtension)
//        def mavenRepoUrl = settings.getProfiles().get(0).getRepositories().get(0).url;
//        // TODO JHE
//        return new ApgRepo("todo","todo","todo","todo")
//    }

//    private void mapMavenLocalRepo(MavenSettingsPluginExtension mavenSettingsPluginExtension) {
//        project.repositories.remove(project.repositories.findByName("MavenLocal"))
//        MavenArtifactRepository mavenLocal = project.getRepositories().mavenLocal();
//        mavenLocal.setUrl(settings.getLocalRepository());
//        project.getRepositories().add(mavenLocal);
//        println "to be deleted"
//    }
}
