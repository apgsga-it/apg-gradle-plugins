#!/bin/bash
./gradlew clean buildZip   -PinstallTarget=CHEI212 -PbomLastRevision=SNAPSHOT  --stacktrace --info
