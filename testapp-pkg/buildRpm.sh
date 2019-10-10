#!/bin/bash
./gradlew clean buildRpm apgGenericPublish --info -PinstallTarget=CHEI212  -Pversion=1.0 -PreleaseNr=1