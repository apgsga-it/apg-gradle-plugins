#!/bin/bash
cd ../../modules/testapp-bom
mvn clean install -Dmaven.repo.local=../../jenkins-pipeline-tests/mavenTestLocalRepo
cd ../testapp-parentpom
mvn clean install -Dmaven.repo.local=../../jenkins-pipeline-tests/mavenTestLocalRepo
cd ../testapp-module
mvn clean install -Dmaven.repo.local=../../jenkins-pipeline-tests/mavenTestLocalRepo
cd ../testapp-service
mvn clean install -Dmaven.repo.local=../../jenkins-pipeline-tests/mavenTestLocalRepo
rm -rf ../../jenkins-pipeline-tests/mavenTestLocalRepo/com/apgsga