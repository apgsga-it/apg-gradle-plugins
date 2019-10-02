# Todos

- [x] Sepcific plugin development repo for each support typ / target
- [x] Sepcific User for the automated Tests
- [x] Refactor com.apgsga.gradle.publish.extension into seperate project, which can be used by different Plugin's
- [x] Adopt current Publishing Plugin's to work with the common configuration
- [x] Verify configuration on the fly pattern with explicit function in Extension. Are there better way's to achieve this?
- [ ] ALL: Obfuscate passwords and their usages
- [x] Make ApgPublishPlugin work generally for artefacts of supported types apart from Maven Artefacts, like tar.gz and rpm. 
- [ ] JHE Test Artefact Typ for example rpm against Metadata of Target repo. 
- [ ] Test rpm publication against yum install
- [ ] JHE / CHE Better Asserts for the functional Tests 
- [ ] JHE Better Nop Logging 
- [ ] JHE Apg Java Base Plugin : standardizes  Apg specific Java Requirements
- [ ] STB Apg Xtend Plugin : standardizes  Apg specific Java Requirements
- [x] CHE : Decide on Github, ev. move to Apg Github or Apg Cvs
- [ ] ALL: Externalize user & password default configuration
- [x] Better Spock Functional- & Integration Test Setup & Clean-up
- [ ] CHE Get rid of System.out.println logging
- [x] Establish Rpm Service Packager Plugin Project -> rpm-packager und DSL, see other open TODO's
- [ ] CHE Apg Dependency Recommender Plugin based on Gradle 5.x Dependency Recommendation Api, which fulfils current Patch Pipeline requirements and also merging of different Version Provider Sources : bom's , patchfile, with precedence ordering
- [ ] JHE Apg Repository Extension Plugin using also the common-repo Plugin
- [ ] JHE Add mavenLocal to maven-publish 
- [x] Verify maven-publish and generic-publish DSL against requirements and use cases 
- [ ] ALL Verify Naming standards for Plugins : package, Plugin Names , Metadata etc
- [x] CHE Multi Project Build, but where each Plugin can be buildt seperately
- [x] CHE Packaging of rpm-packager template files : zip!? 
- [x] rpm-packager first shot: 1 : 1 copy tasks of service-rpm-packager -> better merged into one? Resolution: no, actually the granluar Copy Task make it easier to Test the Plugin
- [x] rpm-packager copy tasks should declare output 
- [ ] JHE rpm-packager : retrieve repoBaseUrl from common-repo plugin
- [x] CHE rpm-packager : copy binary dependencies to lib
- [x] CHE rpm-packager : ospackage resp buildrpm wrapper, first cut
- [x] CHE rpm-packager : test project zum Testen
- [ ] ALL rpm-packager : review konfigurations DSL
- [ ] JHE Build Plugin's in Repo deployen -> eigenes Repository 
- [ ] gradle copy tasks get a : is not up-to-date because:  No history is available. ->  Investigate.
- [ ] ALL Discuss and Define How best to configure defined service Names for Portnr Calculation.
- [ ] JHE WIP: gradle tasks : migrate from create to register api
- [x] Estabilish Plugin Builds on jenkins.apgsga.ch
- [ ] JHE Migrate to newer Gradle Version then 5.0 
- [ ] JHE rpm-packager : BinariesCopyTask make configuration.excludes configurable via extension
- [ ] ALL rpm-packager : Validation & Review of Default Configuration
- [ ] JHE Apg Repository Plugin : remove direct repository configuration usage in Plugins and Functional Tests, use repository Plugin instead
- [ ] Migratation current Gradle Build scripts , apart from Patch System to Apg Plugins
= [ ] Use Apg Plugins for building them selfs
- [ ] Tests with a multi project build 
- [ ] maven-publish : attach and deploy sourcesJar - Task and Publish 
- [ ] generic-public : probably does'nt need Extension -> move the properties to Task properties, with lazy evaluation of Archive File




[ ] WIP:  means in work in progress

[x] means completed
