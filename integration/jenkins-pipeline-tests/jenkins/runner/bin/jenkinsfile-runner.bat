@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\io\jenkins\jenkinsfile-runner\bootstrap\1.0-beta-11\bootstrap-1.0-beta-11.jar;"%REPO%"\args4j\args4j\2.33\args4j-2.33.jar;"%REPO%"\commons-io\commons-io\2.6\commons-io-2.6.jar;"%REPO%"\org\jenkins-ci\version-number\1.6\version-number-1.6.jar;"%REPO%"\io\jenkins\jenkinsfile-runner\setup\1.0-beta-11\setup-1.0-beta-11.jar;"%REPO%"\org\eclipse\jetty\jetty-servlet\9.4.5.v20170502\jetty-servlet-9.4.5.v20170502.jar;"%REPO%"\org\eclipse\jetty\jetty-security\9.4.5.v20170502\jetty-security-9.4.5.v20170502.jar;"%REPO%"\org\eclipse\jetty\jetty-server\9.4.5.v20170502\jetty-server-9.4.5.v20170502.jar;"%REPO%"\javax\servlet\javax.servlet-api\3.1.0\javax.servlet-api-3.1.0.jar;"%REPO%"\org\eclipse\jetty\jetty-http\9.4.5.v20170502\jetty-http-9.4.5.v20170502.jar;"%REPO%"\org\eclipse\jetty\jetty-io\9.4.5.v20170502\jetty-io-9.4.5.v20170502.jar;"%REPO%"\org\eclipse\jetty\jetty-webapp\9.4.5.v20170502\jetty-webapp-9.4.5.v20170502.jar;"%REPO%"\org\eclipse\jetty\jetty-xml\9.4.5.v20170502\jetty-xml-9.4.5.v20170502.jar;"%REPO%"\org\eclipse\jetty\jetty-util\9.4.5.v20170502\jetty-util-9.4.5.v20170502.jar;"%REPO%"\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;"%REPO%"\io\jenkins\lib\support-log-formatter\1.0\support-log-formatter-1.0.jar;"%REPO%"\org\jenkins-ci\main\jenkins-war\2.176.2\jenkins-war-2.176.2.war;"%REPO%"\org\jenkins-ci\main\jenkins-core\2.176.2\jenkins-core-2.176.2.jar;"%REPO%"\org\jenkins-ci\plugins\icon-shim\icon-set\1.0.5\icon-set-1.0.5.jar;"%REPO%"\org\jenkins-ci\main\cli\2.176.2\cli-2.176.2.jar;"%REPO%"\org\jenkins-ci\crypto-util\1.1\crypto-util-1.1.jar;"%REPO%"\org\jvnet\hudson\jtidy\4aug2000r7-dev-hudson-1\jtidy-4aug2000r7-dev-hudson-1.jar;"%REPO%"\com\google\inject\guice\4.0\guice-4.0.jar;"%REPO%"\javax\inject\javax.inject\1\javax.inject-1.jar;"%REPO%"\aopalliance\aopalliance\1.0\aopalliance-1.0.jar;"%REPO%"\org\jruby\ext\posix\jna-posix\1.0.3-jenkins-1\jna-posix-1.0.3-jenkins-1.jar;"%REPO%"\com\github\jnr\jnr-posix\3.0.45\jnr-posix-3.0.45.jar;"%REPO%"\com\github\jnr\jnr-ffi\2.1.8\jnr-ffi-2.1.8.jar;"%REPO%"\com\github\jnr\jffi\1.2.17\jffi-1.2.17.jar;"%REPO%"\com\github\jnr\jffi\1.2.16\jffi-1.2.16-native.jar;"%REPO%"\org\ow2\asm\asm\5.0.3\asm-5.0.3.jar;"%REPO%"\org\ow2\asm\asm-commons\5.0.3\asm-commons-5.0.3.jar;"%REPO%"\org\ow2\asm\asm-analysis\5.0.3\asm-analysis-5.0.3.jar;"%REPO%"\org\ow2\asm\asm-tree\5.0.3\asm-tree-5.0.3.jar;"%REPO%"\org\ow2\asm\asm-util\5.0.3\asm-util-5.0.3.jar;"%REPO%"\com\github\jnr\jnr-x86asm\1.0.2\jnr-x86asm-1.0.2.jar;"%REPO%"\com\github\jnr\jnr-constants\0.9.9\jnr-constants-0.9.9.jar;"%REPO%"\org\kohsuke\trilead-putty-extension\1.2\trilead-putty-extension-1.2.jar;"%REPO%"\org\jenkins-ci\trilead-ssh2\build-217-jenkins-14\trilead-ssh2-build-217-jenkins-14.jar;"%REPO%"\org\connectbot\jbcrypt\jbcrypt\1.0.0\jbcrypt-1.0.0.jar;"%REPO%"\org\kohsuke\stapler\stapler-groovy\1.257.2\stapler-groovy-1.257.2.jar;"%REPO%"\org\kohsuke\stapler\stapler-jelly\1.257.2\stapler-jelly-1.257.2.jar;"%REPO%"\org\jenkins-ci\commons-jelly\1.1-jenkins-20120928\commons-jelly-1.1-jenkins-20120928.jar;"%REPO%"\org\jenkins-ci\dom4j\dom4j\1.6.1-jenkins-4\dom4j-1.6.1-jenkins-4.jar;"%REPO%"\org\kohsuke\stapler\stapler-jrebel\1.257.2\stapler-jrebel-1.257.2.jar;"%REPO%"\org\kohsuke\stapler\stapler\1.257.2\stapler-1.257.2.jar;"%REPO%"\javax\annotation\javax.annotation-api\1.2\javax.annotation-api-1.2.jar;"%REPO%"\commons-discovery\commons-discovery\0.4\commons-discovery-0.4.jar;"%REPO%"\org\jvnet\tiger-types\2.2\tiger-types-2.2.jar;"%REPO%"\org\kohsuke\asm5\5.0.1\asm5-5.0.1.jar;"%REPO%"\org\kohsuke\windows-package-checker\1.2\windows-package-checker-1.2.jar;"%REPO%"\org\kohsuke\stapler\stapler-adjunct-zeroclipboard\1.3.5-1\stapler-adjunct-zeroclipboard-1.3.5-1.jar;"%REPO%"\org\kohsuke\stapler\stapler-adjunct-timeline\1.5\stapler-adjunct-timeline-1.5.jar;"%REPO%"\org\kohsuke\stapler\stapler-adjunct-codemirror\1.3\stapler-adjunct-codemirror-1.3.jar;"%REPO%"\io\jenkins\stapler\jenkins-stapler-support\1.1\jenkins-stapler-support-1.1.jar;"%REPO%"\com\infradna\tool\bridge-method-annotation\1.13\bridge-method-annotation-1.13.jar;"%REPO%"\org\kohsuke\stapler\json-lib\2.4-jenkins-2\json-lib-2.4-jenkins-2.jar;"%REPO%"\net\sf\ezmorph\ezmorph\1.0.6\ezmorph-1.0.6.jar;"%REPO%"\commons-httpclient\commons-httpclient\3.1-jenkins-1\commons-httpclient-3.1-jenkins-1.jar;"%REPO%"\junit\junit\4.12\junit-4.12.jar;"%REPO%"\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;"%REPO%"\org\jenkins-ci\bytecode-compatibility-transformer\2.0-beta-2\bytecode-compatibility-transformer-2.0-beta-2.jar;"%REPO%"\org\kohsuke\asm6\6.2\asm6-6.2.jar;"%REPO%"\org\jenkins-ci\task-reactor\1.5\task-reactor-1.5.jar;"%REPO%"\org\jvnet\localizer\localizer\1.26\localizer-1.26.jar;"%REPO%"\antlr\antlr\2.7.6\antlr-2.7.6.jar;"%REPO%"\org\jvnet\hudson\xstream\1.4.7-jenkins-1\xstream-1.4.7-jenkins-1.jar;"%REPO%"\xpp3\xpp3\1.1.4c\xpp3-1.1.4c.jar;"%REPO%"\net\sf\kxml\kxml2\2.3.0\kxml2-2.3.0.jar;"%REPO%"\jfree\jfreechart\1.0.9\jfreechart-1.0.9.jar;"%REPO%"\jfree\jcommon\1.0.12\jcommon-1.0.12.jar;"%REPO%"\org\apache\ant\ant\1.9.2\ant-1.9.2.jar;"%REPO%"\org\apache\ant\ant-launcher\1.9.2\ant-launcher-1.9.2.jar;"%REPO%"\commons-lang\commons-lang\2.6\commons-lang-2.6.jar;"%REPO%"\commons-digester\commons-digester\2.1\commons-digester-2.1.jar;"%REPO%"\commons-beanutils\commons-beanutils\1.9.3\commons-beanutils-1.9.3.jar;"%REPO%"\org\apache\commons\commons-compress\1.10\commons-compress-1.10.jar;"%REPO%"\javax\mail\mail\1.4.4\mail-1.4.4.jar;"%REPO%"\org\jvnet\hudson\activation\1.1.1-hudson-1\activation-1.1.1-hudson-1.jar;"%REPO%"\jaxen\jaxen\1.1-beta-11\jaxen-1.1-beta-11.jar;"%REPO%"\commons-jelly\commons-jelly-tags-fmt\1.0\commons-jelly-tags-fmt-1.0.jar;"%REPO%"\commons-jelly\commons-jelly-tags-xml\1.1\commons-jelly-tags-xml-1.1.jar;"%REPO%"\org\jvnet\hudson\commons-jelly-tags-define\1.0.1-hudson-20071021\commons-jelly-tags-define-1.0.1-hudson-20071021.jar;"%REPO%"\org\jenkins-ci\commons-jexl\1.1-jenkins-20111212\commons-jexl-1.1-jenkins-20111212.jar;"%REPO%"\org\acegisecurity\acegi-security\1.0.7\acegi-security-1.0.7.jar;"%REPO%"\org\springframework\spring-jdbc\1.2.9\spring-jdbc-1.2.9.jar;"%REPO%"\org\springframework\spring-dao\1.2.9\spring-dao-1.2.9.jar;"%REPO%"\oro\oro\2.0.8\oro-2.0.8.jar;"%REPO%"\log4j\log4j\1.2.9\log4j-1.2.9.jar;"%REPO%"\org\codehaus\groovy\groovy-all\2.4.12\groovy-all-2.4.12.jar;"%REPO%"\jline\jline\2.12\jline-2.12.jar;"%REPO%"\org\fusesource\jansi\jansi\1.11\jansi-1.11.jar;"%REPO%"\org\springframework\spring-webmvc\2.5.6.SEC03\spring-webmvc-2.5.6.SEC03.jar;"%REPO%"\org\springframework\spring-beans\2.5.6.SEC03\spring-beans-2.5.6.SEC03.jar;"%REPO%"\org\springframework\spring-context\2.5.6.SEC03\spring-context-2.5.6.SEC03.jar;"%REPO%"\org\springframework\spring-context-support\2.5.6.SEC03\spring-context-support-2.5.6.SEC03.jar;"%REPO%"\org\springframework\spring-web\2.5.6.SEC03\spring-web-2.5.6.SEC03.jar;"%REPO%"\org\springframework\spring-core\2.5.6.SEC03\spring-core-2.5.6.SEC03.jar;"%REPO%"\org\springframework\spring-aop\2.5.6.SEC03\spring-aop-2.5.6.SEC03.jar;"%REPO%"\javax\servlet\jsp\jstl\javax.servlet.jsp.jstl-api\1.2.1\javax.servlet.jsp.jstl-api-1.2.1.jar;"%REPO%"\org\slf4j\jcl-over-slf4j\1.7.25\jcl-over-slf4j-1.7.25.jar;"%REPO%"\org\slf4j\log4j-over-slf4j\1.7.25\log4j-over-slf4j-1.7.25.jar;"%REPO%"\com\sun\xml\txw2\txw2\20110809\txw2-20110809.jar;"%REPO%"\javax\xml\stream\stax-api\1.0-2\stax-api-1.0-2.jar;"%REPO%"\relaxngDatatype\relaxngDatatype\20020414\relaxngDatatype-20020414.jar;"%REPO%"\org\jvnet\winp\winp\1.28\winp-1.28.jar;"%REPO%"\org\jenkins-ci\memory-monitor\1.9\memory-monitor-1.9.jar;"%REPO%"\org\codehaus\woodstox\wstx-asl\3.2.9\wstx-asl-3.2.9.jar;"%REPO%"\stax\stax-api\1.0.1\stax-api-1.0.1.jar;"%REPO%"\org\jenkins-ci\jmdns\3.4.0-jenkins-3\jmdns-3.4.0-jenkins-3.jar;"%REPO%"\net\java\dev\jna\jna\4.5.2\jna-4.5.2.jar;"%REPO%"\org\kohsuke\akuma\1.10\akuma-1.10.jar;"%REPO%"\org\kohsuke\libpam4j\1.11\libpam4j-1.11.jar;"%REPO%"\org\kohsuke\libzfs\0.8\libzfs-0.8.jar;"%REPO%"\com\sun\solaris\embedded_su4j\1.1\embedded_su4j-1.1.jar;"%REPO%"\net\java\sezpoz\sezpoz\1.13\sezpoz-1.13.jar;"%REPO%"\org\kohsuke\jinterop\j-interop\2.0.6-kohsuke-1\j-interop-2.0.6-kohsuke-1.jar;"%REPO%"\org\kohsuke\jinterop\j-interopdeps\2.0.6-kohsuke-1\j-interopdeps-2.0.6-kohsuke-1.jar;"%REPO%"\org\samba\jcifs\jcifs\1.2.19\jcifs-1.2.19.jar;"%REPO%"\org\jvnet\robust-http-client\robust-http-client\1.2\robust-http-client-1.2.jar;"%REPO%"\commons-codec\commons-codec\1.9\commons-codec-1.9.jar;"%REPO%"\org\kohsuke\access-modifier-annotation\1.14\access-modifier-annotation-1.14.jar;"%REPO%"\commons-fileupload\commons-fileupload\1.3.1-jenkins-2\commons-fileupload-1.3.1-jenkins-2.jar;"%REPO%"\com\google\guava\guava\11.0.1\guava-11.0.1.jar;"%REPO%"\com\jcraft\jzlib\1.1.3-kohsuke-1\jzlib-1.1.3-kohsuke-1.jar;"%REPO%"\org\jenkins-ci\main\remoting\3.29\remoting-3.29.jar;"%REPO%"\org\jenkins-ci\constant-pool-scanner\1.2\constant-pool-scanner-1.2.jar;"%REPO%"\org\jenkins-ci\modules\instance-identity\2.2\instance-identity-2.2.jar;"%REPO%"\io\github\stephenc\crypto\self-signed-cert-generator\1.0.0\self-signed-cert-generator-1.0.0.jar;"%REPO%"\org\jenkins-ci\modules\ssh-cli-auth\1.5\ssh-cli-auth-1.5.jar;"%REPO%"\org\jenkins-ci\modules\slave-installer\1.6\slave-installer-1.6.jar;"%REPO%"\org\jenkins-ci\modules\windows-slave-installer\1.10.0\windows-slave-installer-1.10.0.jar;"%REPO%"\org\jenkins-ci\modules\launchd-slave-installer\1.2\launchd-slave-installer-1.2.jar;"%REPO%"\org\jenkins-ci\modules\upstart-slave-installer\1.1\upstart-slave-installer-1.1.jar;"%REPO%"\org\jenkins-ci\modules\systemd-slave-installer\1.1\systemd-slave-installer-1.1.jar;"%REPO%"\org\jenkins-ci\modules\sshd\2.6\sshd-2.6.jar;"%REPO%"\org\apache\sshd\sshd-core\1.7.0\sshd-core-1.7.0.jar;"%REPO%"\net\i2p\crypto\eddsa\0.3.0\eddsa-0.3.0.jar;"%REPO%"\org\jenkins-ci\ui\jquery-detached\1.2.1\jquery-detached-1.2.1-core-assets.jar;"%REPO%"\org\jenkins-ci\ui\bootstrap\1.3.2\bootstrap-1.3.2-core-assets.jar;"%REPO%"\org\jenkins-ci\ui\jquery-detached\1.2.1\jquery-detached-1.2.1.jar;"%REPO%"\org\jenkins-ci\ui\handlebars\1.1.1\handlebars-1.1.1-core-assets.jar;"%REPO%"\io\jenkins\jenkinsfile-runner\payload\1.0-beta-11\payload-1.0-beta-11.jar;"%REPO%"\io\jenkins\jenkinsfile-runner\payload-dependencies\1.0-beta-11\payload-dependencies-1.0-beta-11.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-cps\2.74\workflow-cps-2.74.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-step-api\2.20\workflow-step-api-2.20.jar;"%REPO%"\org\jenkins-ci\plugins\structs\1.20\structs-1.20.jar;"%REPO%"\com\cloudbees\groovy-cps\1.30\groovy-cps-1.30.jar;"%REPO%"\org\jenkins-ci\ui\ace-editor\1.1\ace-editor-1.1.jar;"%REPO%"\com\cloudbees\diff4j\1.2\diff4j-1.2.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-api\2.37\workflow-api-2.37.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-job\2.35\workflow-job-2.35.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-multibranch\2.21\workflow-multibranch-2.21.jar;"%REPO%"\org\jenkins-ci\plugins\branch-api\2.5.4\branch-api-2.5.4.jar;"%REPO%"\org\jenkins-ci\plugins\cloudbees-folder\6.9\cloudbees-folder-6.9.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-scm-step\2.9\workflow-scm-step-2.9.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-basic-steps\2.18\workflow-basic-steps-2.18.jar;"%REPO%"\org\jenkins-ci\plugins\mailer\1.28\mailer-1.28.jar;"%REPO%"\org\jenkins-ci\plugins\display-url-api\2.3.2\display-url-api-2.3.2.jar;"%REPO%"\org\jenkins-ci\plugins\apache-httpcomponents-client-4-api\4.5.10-1.0\apache-httpcomponents-client-4-api-4.5.10-1.0.jar;"%REPO%"\org\apache\httpcomponents\httpclient\4.5.10\httpclient-4.5.10.jar;"%REPO%"\org\apache\httpcomponents\httpcore\4.4.12\httpcore-4.4.12.jar;"%REPO%"\org\apache\httpcomponents\httpmime\4.5.10\httpmime-4.5.10.jar;"%REPO%"\org\apache\httpcomponents\fluent-hc\4.5.10\fluent-hc-4.5.10.jar;"%REPO%"\org\apache\httpcomponents\httpclient-cache\4.5.10\httpclient-cache-4.5.10.jar;"%REPO%"\org\apache\httpcomponents\httpasyncclient\4.1.4\httpasyncclient-4.1.4.jar;"%REPO%"\org\apache\httpcomponents\httpcore-nio\4.4.10\httpcore-nio-4.4.10.jar;"%REPO%"\org\apache\httpcomponents\httpasyncclient-cache\4.1.4\httpasyncclient-cache-4.1.4.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-durable-task-step\2.34\workflow-durable-task-step-2.34.jar;"%REPO%"\org\jenkins-ci\plugins\durable-task\1.30\durable-task-1.30.jar;"%REPO%"\org\jenkins-ci\plugins\workflow\workflow-support\3.3\workflow-support-3.3.jar;"%REPO%"\org\jboss\marshalling\jboss-marshalling-river\2.0.6.Final\jboss-marshalling-river-2.0.6.Final.jar;"%REPO%"\org\jboss\marshalling\jboss-marshalling\2.0.6.Final\jboss-marshalling-2.0.6.Final.jar;"%REPO%"\org\jenkins-ci\plugins\script-security\1.64\script-security-1.64.jar;"%REPO%"\org\kohsuke\groovy-sandbox\1.23\groovy-sandbox-1.23.jar;"%REPO%"\com\github\ben-manes\caffeine\caffeine\2.7.0\caffeine-2.7.0.jar;"%REPO%"\org\checkerframework\checker-qual\2.6.0\checker-qual-2.6.0.jar;"%REPO%"\com\google\errorprone\error_prone_annotations\2.3.3\error_prone_annotations-2.3.3.jar;"%REPO%"\org\jenkins-ci\plugins\scm-api\2.6.3\scm-api-2.6.3.jar;"%REPO%"\org\slf4j\slf4j-jdk14\1.7.25\slf4j-jdk14-1.7.25.jar;"%REPO%"\org\slf4j\slf4j-api\1.7.25\slf4j-api-1.7.25.jar;"%REPO%"\commons-collections\commons-collections\3.2.2\commons-collections-3.2.2.jar;"%REPO%"\org\jenkins-ci\symbol-annotation\1.20\symbol-annotation-1.20.jar;"%REPO%"\org\jenkins-ci\annotation-indexer\1.12\annotation-indexer-1.12.jar;"%REPO%"\com\google\code\findbugs\annotations\3.0.1\annotations-3.0.1.jar;"%REPO%"\net\jcip\jcip-annotations\1.0\jcip-annotations-1.0.jar;"%REPO%"\com\google\code\findbugs\jsr305\3.0.1\jsr305-3.0.1.jar;"%REPO%"\io\jenkins\jenkinsfile-runner\jenkinsfile-runner\1.0-beta-11\jenkinsfile-runner-1.0-beta-11.jar

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS%  -classpath %CLASSPATH% -Dapp.name="jenkinsfile-runner" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" io.jenkins.jenkinsfile.runner.bootstrap.Bootstrap %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
