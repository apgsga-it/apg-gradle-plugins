# Example Packaging Project for a Apg Service 

This is a example Packaging Project for a Echoservice, see ../testapp-service

It is a Gradle Build, which uses the Apg rpm-packager plugin, the gradle-ssh-plugin & the gradle-credentials-plugin

It produces from a number of parameters a RPM package for a given service and installs it on the target host.

## Plugins used apart from the Apg Plugins

* gradle-ssh-plugin https://gradle-ssh-plugin.github.io/docs/
* gradle-credentials-plugin: https://github.com/etiennestuder/gradle-credentials-plugin

## build.gradle

The build script build.script supports the following parameters

* serviceName: The name of the service to be packaged and deployed 
* targetHost: the host on which the rpm package is installed
* sshUser: The ssh user with which the rpm will be deploy 
* sshPw:
* installTarget: The enviroment for which the service is packaged and installed
* serviceVersion: the rpm package version 
* releaseNr: the rpm release version 
* downloadDir: a directory on the targetHost to which the rpm package is transfered and from which it is installed

Apart from the parameters the build script paramerizes the gradle extension apgRpmPackage according to the requirements of the service: 

```gradle
apgRpmPackage {
	serviceName = project.ext.serviceName
	// TODO (che,15.10) jadas-e services, neecs to be discussed how this list is maintained
	supportedServices = ["jadas", "digiflex","vkjadas", "interjadas", "interweb", project.ext.serviceName]
	dependencies = [
		"com.apgsga:testapp-service:0.1-SNAPSHOT"
	]
	resourceFilters = "serviceport"
	appConfigFilters = "general"
  	servicePropertiesDir = "resources"
	installTarget = project.ext.installTarget
	mainProgramName  = "com.apgsga.testapp.TestappApplication"
	version = project.ext.serviceVersion 
	releaseNr = project.ext.releaseNr
}
```

## resources Directory 

Here additional service specific properties file can be put. Only appconfig.properties and resource.properties are considered by the packaging process. They can be nested. 

Example appconfig.properties: 

```java 
# Some service specific app properities
message=hello
```


## Jenkinsfile

The Jenkinsfile for a Jenkins Pipeline. The Pipeline basically mirrors the parameters of the Gradle build.script
It uses the Jenkins Pipeline Credentials Binding Plugin, see also https://jenkins.io/doc/pipeline/steps/credentials-binding/
It assumed the existence of a usernamePassword credentialsId of : jadas-e-ssh

Appart from the Credentials Handling it simply executes the Gradle build script 

As additional parameters the JenkinsFile supports:

* REPO: which is a Jenkins Pipeline scm url. Currently the script only supports Git repositories (GitScm). It supports any valid url for git clone. Examples for Urls: https://github.com/apgsga-it/apg-gradle-plugins.git, also ssh://chhex@192.168.1.34/Users/chhex/git/apg-gradle-plugins 
* BRANCH: any valid git branch, eg master

## config.xml 

This is a copy of the Jenkins Job Configruration file, which has been used for testing.
It is a Jenkins Pipeline Job, which supports the Parameters of the Jenkinsfile and provides Defaults

## Jenkins preconditions

Depending how ssh is installed on the jenkins.apgsga.ch, the user under which jenkins is running may need a valid ssh host key for the target host: jadas-e.apgsga.h
At least the gradle ssh plugin is configured to do strict knownhost checking. 

## TODOS

- [ ] Discuss and decide on the portnr algorithm, specifically on the maintenance of the services list
- [ ] Is the gradle-ssh-plugin to be configured with strict knownhost checking for ssh
- [ ] Support also cvs as scm apart from git