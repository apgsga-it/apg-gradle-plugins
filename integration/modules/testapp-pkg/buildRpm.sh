#!/bin/bash
./gradlew clean buildRpm   -PinstallTarget=CHEI212 -PbomLastRevision=SNAPSHOT -PrpmReleaseNr=3 --stacktrace --info

