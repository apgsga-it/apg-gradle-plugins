#!/bin/bash
./gradlew createNewBomRevision -PinstallTarget=CHEI212 -PbaseVersion=1.0 -PbomLastRevision=SNAPSHOT  --stacktrace --info