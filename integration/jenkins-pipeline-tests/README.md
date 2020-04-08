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

Note: the Installation of the Jenkinsfile Runner does not have to be in
the project directory

### Directory Structure

#### Jenkins Runner
{installdir} = Installation Directory , where the Runner is installed
- **${installdir}/runner/bin** : *The executable fat jar of the
  jenkinsfile-runner*
- **{installdir}/runner/jenkins** : *Currently a pure vanilla packaging
  of Jenkins , ratio: find out what we \ what we really need of the
  Jenkins Plugins*
- **{installdir}/jenkins/war** : *the jenkins.war*
- **{installdir}/jenkins/plugins** : *plugins of the current Jenkins
  container tested*

### `Packaging` of the Jenkins File Runner

The installation of a jenkinsfile-runner is automated by the script

`./installrunner.sh `

with `./installrunner.sh --help' Usage information is printed:
```
./installrunner.sh --help Usage: installrunner.sh [-d BUILDDIR]
[-r GITREPO] [-b BRANCH] [-i INSTALLDIR] -ns Builds and Installs the
jenkinsfile-runner build into a installation Dir to be used with the
Jenkinspipeline Tests

    -h          display this help and exit
    -d=BUILDDIR target of the git clone of the git repo
    -r=GITREPO  git repo, from which the jenkinsfile runner will be cloned
    -b=BRANCH   git of the git repo
    -i=INSTALLDIR Installation Dir of the jenkinsfile runner
    -n          do not delete and clone the Builddir, if it exists
    -s          skip maven package of jenkinsfile-runner
```

which automates the following steps by example

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

The Jenkins File Runner Repo is cloned from Github , built and the
resulting artifacts copied accordingly

NOTE as of 7/4/2020: The clone is currently done from a forked Version
in the Apg repo, since the master version of the source is broken
    
### Jenkins Casc

Is set as parameter on the Test Task, for example:

```
task runTestLibHelloWorld(type: JenkinsRunnerExec) { workspaceDir =
"jenkinsHelloWorldLib" environment =
[CASC_JENKINS_CONFIG:"${project.projectDir}/jenkinsHelloWorldLib/jenkins.yaml"]
description = "Runs Pipeline in ${workspaceDir} , which loads the
Pipeline libarary from
https://github.com/chhex/jenkins-pipeline-testlib.git "

}
```
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
