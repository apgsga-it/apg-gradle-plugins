#!/bin/bash
./gradlew deployRpm  -PinstallTarget=CHEI212  -PbomLastRevision=SNAPSHOT -PrpmReleaseNr=1 -PtargetHost=dev-jhedocker.light.apgsga.ch  --stacktrace --info
