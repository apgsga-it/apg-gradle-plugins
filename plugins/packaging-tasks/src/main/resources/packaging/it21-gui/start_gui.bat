    @echo off
set path=%~dp0.\\dll;%path%
start javaw -client ${javaOpts} -cp "./bin;./lib/*;./shared/*;./conf" ${mainProgramm}