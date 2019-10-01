# gradle-plugins
A collection of  opinated Gradle Plugins, which short cut configuration of others plugins usage with standardized defaults which can be reconfigured and tasks.

## common-repo

Provides two extensions, which configures the Repositories used in Apg, which reasonable defaults: 

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

