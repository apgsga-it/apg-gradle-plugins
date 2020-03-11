Packageing Jenkinsfile Runner
-----------------------------

See also
[github.com/jenkinsci jenkinsfile-runner](https://github.com/jenkinsci/jenkinsfile-runner)

### Directory Structure

- **bin** : *scripts for starting the Jenkinsfile Runner (shell & bat)*
- **lib** : *all jars necessary to start the Jenkinsfile Runner*
- **jenkins** : *jenkins runtime requirements for the Jenkinsfile
  Runner*
- **jenkins\lib** : *the jenkins.war of the current Jenkins container
  tested*
- **jenkins\plugins** : *all the plugins of the current Jenkins
  container tested*

### `Packaging`

#### Jenkins

The content of the **jenkins** directory is downloaded by Gradle Tasks
from the target installation of jenkins

For example with jenkins.apgsga.ch :

- **lib** : jenkins.war from /usr/lib/jenkins/jenkins.war
- **plugins** : the whole directory /var/jenkins/plugins/

#### Jenkinsfile Runner

The version is downloaded from the Jenkins
[Binary Repo](https://repo.jenkins-ci.org/webapp/#/home)

The search for the artefact: *jenkinsfile-runner* The current version is
repo/io/jenkins/jenkinsfile-runner/jenkinsfile-runner/1.0-beta-11/jenkinsfile-runner-1.0-beta-11.jar

The following steps:


1. Download the
   [zip](releases/io/jenkins/jenkinsfile-runner/jenkinsfile-runner/1.0-beta-11/jenkinsfile-runner-1.0-beta-11.zip)<!-- @IGNORE PREVIOUS: link -->
2. Unzip into a temp directory
3. Flatten the directory with all jars and exclude all plugin folders
   into a new lib directory
4. Copy this lib and the bin into the target folder
5. Edit in the bin scripts the classpath to use lib/* instead of all
   jars explicitly

Point 3. can be done with find for example or the cool tool
[fd](https://github.com/sharkdp/fd) with the following command:

`fd -HI -e jar -E plugin -x cp {} lib`


