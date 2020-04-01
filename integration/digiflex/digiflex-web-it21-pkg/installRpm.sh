#!/bin/bash
./gradlew installRpm  -PinstallTarget=CHEI212 -PbaseVersion=1.0 -PbomLastRevision=SNAPSHOT -PrpmReleaseNr=5 -PtargetHost=172.16.92.139 -PsshUser=che -PsshPw=chePw  --stacktrace --info
