#!/bin/bash
./gradlew installRpm  -PinstallTarget=CHEI212  -PbomLastRevision=SNAPSHOT -PrpmReleaseNr=1 -PtargetHost=dev-jhedocker.light.apgsga.ch  --stacktrace --info
