Testing with Jenkinsfile Runner
-------------------------------

This module supports the Testing of the combination of different Jenkins
Pipeline , Pipeline Library Functionality and Input data.

### Run Tests

For the available Test tasks, see

`./gradlew tasks --group="Apg Gradle Jenkinsrunner"
`

### Create new Tests

1. Create new subdirectory containing the Jenkinsfile to Test
2. Create a new Task of JenkinsRunnerExec Typ in the build and parametrize accordingly ,see below

### Parameters of the JenkinsRunnerExec Task

- jenkinsWorkspaceDirPath : the directory where the Test modules are. The Jenkins Workspace. This basically skips the checking out of the modules
- workspaceDir : the directory, where the Jenkinsfile under Test is
- appArgs : the Parameters to run the Pipeline with
- environment : Enviroment Variable to be set, example `environment =
  [CASC_JENKINS_CONFIG:"${project.projectDir}/runner/config/jenkins.yaml"]
  `
### Packaging of the Jenkinsfile Runner

See also
[github.com/jenkinsci jenkinsfile-runner](https://github.com/jenkinsci/jenkinsfile-runner)

### Directory Structure

#### Jenkins Runner
- **runner/bin** : *The executable fat jar of the jenkinsfile-runner*
- **runner/jenkins** : *Currently a pure vanilla packaging of Jenkins ,
  ratio: find out what we \ what we really need of the Jenkins Plugins*
- **jenkins/war** : *the jenkins.war*
- **jenkins/plugins** : *plugins of the current Jenkins container
  tested*
- **jenkins/config** : all yaml files we need for the
  [Jenkins Casc Plugin](https://github.com/jenkinsci/configuration-as-code-plugin),
  also for configuration of Jenkins Libs

### `Packaging` of the Jenkins File Runner

Currently to create a new Jenkins File Runner version following steps
must be done, by example

1. cd ~/git
2. git clone https://github.com/jenkinsci/jenkinsfile-runner.git
3. cd jenkinsfile-runner
4. mvn clean package
5. cp app/target/jenkinsfile-runner-standalone.jar
   ~/git/apg-gradle-plugins/integration/jenkins-pipeline-tests/runner/bin/
6. cp vanilla-package/target/war/jenkins.war
   ~/git/apg-gradle-plugins/integration/jenkins-pipeline-tests/runner/jenkins/war
7. cp -r vanilla-package/target/plugins/
   ~/git/apg-gradle-plugins/integration/jenkins-pipeline-tests/runner/jenkins/plugins/

Basically the Jenkins File Runner Repo is clone from Github , built and
the resulting artifacts copied accordingly

So the jenkinsfile runner runtime is version controlled in github, with
all the necessary binary artifacts

### Jenkins Casc

Are configure with the
[Jenkins Casc Plugin](https://github.com/jenkinsci/configuration-as-code-plugin).
Currently for example in the config File in
[jenkins.yaml](file:/runner/config/jenkins.yaml)

with this content
```
unclassified:
  globalLibraries:
    libraries:
      - defaultVersion: "0.1"
        name: "test-functions"
        implicit: true
        retriever:
          legacySCM:
            scm:
              git:
                userRemoteConfigs:
                  - url: "https://github.com/chhex/jenkins-pipeline-testlib.git"
                branches:
                  - name: "master"`


```
