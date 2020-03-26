#!/bin/bash
./gradlew clean htmlDependencyReport buildRpm deployRpm --info -PinstallTarget=CHEI212  -PserviceVersion=1.0 -PreleaseNr=16 -PtargetHost=172.16.92.139 -PsshUser=che
