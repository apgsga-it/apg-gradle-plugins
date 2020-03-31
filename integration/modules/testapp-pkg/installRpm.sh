#!/bin/bash
./gradlew installRpm  -PinstallTarget=CHEI212 -PbaseVersion=1.0 -PbomLastRevision=SNAPSHOT -PrpmReleaseNr=3 -PtargetHost=192.168.1.28 -PsshUser=che -PsshPw=chePw  --stacktrace --info
