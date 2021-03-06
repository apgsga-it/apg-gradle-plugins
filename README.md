# gradle-plugins

A collection of  opinated Gradle Plugins, which short cut configuration of others plugins usage with standardized defaults which can be reconfigured and tasks.

## common-repo

Provides two extensions, which configures the Repositories used in Apg with reasonable defaults: 

```
 plugins {
    id 'com.apgsga.common.repo' 
 }
 apgArtifactoryRepo {
 }
 apgLocalRepo {
 }
```

It is in the responsiblity of the applying Plugins, to use those extensions for configuration of their tasks , resp Extensions.

The current extension Values can be logged:

```
apgLocalRepo.log()
apgArtifactoryRepo.log()
```

Which produces with the --info parameter of a gradle build as an example the following output:

```
 Local Repository Configuration is:
    [repoBaseUrl=otherdirectorty, repoName=testrepo, releaseRepoName=maventestrepo, snapshotRepoName=maventestrepo, user=, password=xxxxxx, project=root project 'gradletestproject8124353572715113369']
    Remote Repository Configuration is:
    [repoBaseUrl=xxxx, repoName=remoteRepoName, releaseRepoName=release-functionaltest, snapshotRepoName=snapshot-functionaltest, user=abc, password=xxxxxx, project=root project 'gradletestproject8124353572715113369']
```

The Defaults can be overwritten: 

```
 plugins {
    id 'com.apgsga.common.repo' 
 }
apgArtifactoryRepo {
 repoBaseUrl = "xxxx"
 repoName = "remoteRepoName"
 user = "abc"
 password = "def"
}
apgLocalRepo {
 repoBaseUrl = "otherdirectorty"
 repoName = "testrepo"
}
```

## generic-publish

Generic Plugin is a Plugin for uploading tar.gz , rpm etc artefacts to a binary Repository. It supports currently uploading only to a Artifactory Repository and/or a local Directory. For uploading to Artifactory it uses the https://github.com/jfrog/artifactory-client-java Rest Client. 

To both: 
```
plugins {
  id 'com.apgsga.publish' 
}
apgGenericPublishConfig {
  artefactFile = "${tarToPublish.absolutePath}"
  local()
  artifactory()
}
```

Only to Artifactory:
```
apgGenericPublishConfig {
  artefactFile = "${tarToPublish.absolutePath}"
  artifactory()
}
```
Only to local:

```
apgGenericPublishConfig {
  artefactFile = "${tarToPublish.absolutePath}"
  local()
}
```
The configuration is applied from the common-repo

So this is possible:
```
apgGenericPublishConfig {
  artefactFile = "${tarToPublish.absolutePath}"
  local()
}
apgLocalRepo.log()

```

## maven-publish

Is a Plugin, which dramatically shortscuts Gradle MavenPublications also Artifactory and to a local Repository:

```
   plugins {
     id 'com.apgsga.maven.publish'  
   }
   apgMavenPublish {
     artefactId = "plugin-test"
     version = "1.0-SNAPSHOT"
     groupId = "com.apgsga.gradle.plugins.test"
     local()
     artifactory()
   }
```

The configuration for the repositories is applied from the common-repo


## common-service-packager

TODO 

## rpm-service-packager 

TODO 

## zip-service-packager 

TODO 
