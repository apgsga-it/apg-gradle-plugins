#!/bin/bash
# TODO (che, jhe) : untested
./gradlew deployZip  -PinstallTarget=CHEI212 -PbaseVersion=1.0 -PbomLastRevision=SNAPSHOT-PtargetHost=172.16.92.139 -PsshUser=che -PsshPw=chePw  --stacktrace --info
