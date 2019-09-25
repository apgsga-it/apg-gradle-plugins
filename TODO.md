# Todos

- [x] Sepcific plugin development repo for each support typ / target
- [x] Sepcific User for the automated Tests
- [x] Refactor com.apgsga.gradle.publish.extension into seperate project, which can be used by different Plugin's
- [x] Adopt current Publishing Plugin's to work with the common configuration
- [x] Verify configuration on the fly pattern with explicit function in Extension. Are there better way's to achieve this?
- [ ] Obfuscate passwords and their usages
- [x] Make ApgPublishPlugin work generally for artefacts of supported types apart from Maven Artefacts, like tar.gz and rpm. 
- [ ] Test Artefact Typ for example rpm against Metadata of Target repo. 
- [ ] Test rpm publication against yum install
- [ ] Better Asserts for the functional Tests 
- [ ] Better Nop Logging 
- [ ] Apg Java Base Plugin : standardizes  Apg specific Java Requirements
- [ ] Apg Xtend Plugin : standardizes  Apg specific Java Requirements
- [ ] Decide on Github, ev. move to Apg Github or Apg Cvs
- [ ] Externalize user & password default configuration
- [ ] WIP:  Better Spock Functional- & Integration Test Setup & Clean-up
- [ ] Get rid of System.out.println logging
- [x] Establish Rpm Service Packager Plugin Project -> rpm-packager und DSL, see other open TODO's
- [ ] Apg Dependency Recommender Plugin based on Gradle 5.x Dependency Recommendation Api, which fulfils current Patch Pipeline requirements and also merging of different Version Provider Sources : bom's , patchfile, with precedence ordering
- [ ] Apg Repository Configuration Plugin using also the common-repo Plugin
- [ ] Add mavenLocal to maven-publish 
- [ ] WIP: Verify maven-publish and generic-publish DSL against requirements and use cases 
- [ ] Verify Naming standards for Plugins : package, Plugin Names , Metadata etc
- [ ] Multi Project Build, but where each Plugin can be buildt seperately
- [ ] Packaging of rpm-packager template files : zip!? 
- [x] rpm-packager first shot: 1 : 1 copy tasks of service-rpm-packager -> better merged into one? Resolution: no, actually the granluar Copy Task make it easier to Test the Plugin
- [x] rpm-packager copy tasks should declare output 
- [ ] rpm-packager : retrieve repoBaseUrl from common-repo plugin
- [ ] rpm-packager : copy binary dependencies to lib
- [ ] rpm-packager : ospackage resp buildrpm wrapper 
- [ ] rpm-packager : test project zum Testen
- [ ] rpm-packager : review konfigurations DSL
- [ ] Build Plugin's in Repo deployen -> eigenes Repository 
- [ ] gradle copy tasks get a : is not up-to-date because:  No history is available. ->  Investigate.
- [ ] Discuss and Define How best to configure defined service Names for Portnr Calculation.
- [ ] WIP: gradle tasks : migrate from create to register api
- [x] Estabilish Plugin Builds on jenkins.apgsga.ch
- [ ] Migrate to newer Gradle Version then 5.0 




[ ] WIP:  means in work in progress

[x] means completed
