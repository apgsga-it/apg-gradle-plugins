#!/bin/bash
./gradlew deployRpm  -PinstallTarget=CHEI212  -PbomLastRevision=SNAPSHOT -PrpmReleaseNr=1 -PtargetHost=192.168.1.28 -PsshUser=che -PsshPw=chePw  --stacktrace --info
