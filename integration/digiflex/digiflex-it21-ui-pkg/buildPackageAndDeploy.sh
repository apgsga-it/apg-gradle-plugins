#!/bin/bash
./gradlew zipPackageTask deployZip --info -PinstallTarget=CHEI212  -Pversion=1.0 -PreleaseNr=12 -PtargetHost=192.168.1.36 -PsshUser=che
