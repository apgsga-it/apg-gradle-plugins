Packageing and Testing with Jenkinsfile Runner
----------------------------------------------

See also
[github.com/jenkinsci jenkinsfile-runner](https://github.com/jenkinsci/jenkinsfile-runner)

### Directory Structure

#### Jenkins Runner
- **runner/bin** : *The executable fat jar of the jenkinsfile-runner*
- **jenkins** : *Currently a pure vanilla packaging of Jenkins , ratio:
  find out what we \ what we really need of the Jenkins Plugins*
- **jenkins\war** : *the jenkins.war*
- **jenkins\plugins** : *plugins of the current Jenkins container
  tested*
- **jenkins\config** : all yaml files we need for the
  [Jenkins Casc Plugin](https://github.com/jenkinsci/configuration-as-code-plugin),
  also for configuration of Jenkins Libs
#### Tests Directory

TODO

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

### Testing

TODO

#### Build script

#### Test Cases

