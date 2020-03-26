#!/bin/bash
./gradlew clean buildRpm --info -PinstallTarget=CHEI212  -PserviceVersion=1.0 -PreleaseNr=1 -PtargetHost=172.16.92.139 -PsshUser=che

 
      