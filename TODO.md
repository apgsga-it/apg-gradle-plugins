# Todos

- [x] Sepcific plugin development repo for each support typ / target
- [x] Sepcific User for the automated Tests
- [x] Refactor com.apgsga.gradle.publish.extension into seperate project, which can be used by different Plugin's
- [x] Adopt current Publishing Plugin's to work with the common configuration
- [ ] WIP:  Verify configuration on the fly pattern with explicit function in Extension. Are there better way's to achieve this?
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
- [ ] Rpm Service Packager Plugin Project, initial sketch of a possible Target Dsl
- [ ] Apg Dependency Recommender Plugin based on Gradle 5.x Dependency Recommendation Api, which fulfils current Patch Pipeline requirements and also merging of different Version Provider Sources : bom's , patchfile, with precedence ordering
- [ ] Apg Repository Configuration Plugin using also the common-repo Plugin
- [ ] Add mavenLocal to maven-publish 
- [ ] WIP: Verify maven-publish and generic-publish DSL against requirements and use cases 
- [ ] Verify Naming standards for Plugins : package, Plugin Names , Metadata etc



[ ] WIP:  means in work in progress

[x] means completed
