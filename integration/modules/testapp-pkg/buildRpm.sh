#!/bin/bash
./gradlew clean configureResolutionStrategy buildRpm deployRpm  -PinstallTarget=CHEI212 -PbaseVersion=1.0 -PbomLastRevision=SNAPSHOT -PrpmReleaseNr=3 -PtargetHost=172.16.92.139 -che=che -PsshPw=chePw  --stacktrace --info
