set origDir=%cd%
cd ..\..\modules\testapp-bom
call mvn clean install -Dmaven.repo.local=..\..\jenkins-pipeline-tests\mavenTestLocalRepo
cd ..\testapp-parentpom
call mvn clean install -Dmaven.repo.local=..\..\jenkins-pipeline-tests\mavenTestLocalRepo
cd ..\testapp-module
call mvn clean install -Dmaven.repo.local=..\..\jenkins-pipeline-tests\mavenTestLocalRepo
cd ..\testapp-service
call mvn clean install -Dmaven.repo.local=..\..\jenkins-pipeline-tests\mavenTestLocalRepo
rmdir /s/q ..\..\jenkins-pipeline-tests\mavenTestLocalRepo\com\apgsga
cd %origDir%
