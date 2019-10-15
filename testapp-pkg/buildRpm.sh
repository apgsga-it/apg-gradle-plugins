#!/bin/bash
./gradlew clean buildRpm deployRpm --info -PinstallTarget=CHEI212  -Pversion=1.0 -PreleaseNr=12
