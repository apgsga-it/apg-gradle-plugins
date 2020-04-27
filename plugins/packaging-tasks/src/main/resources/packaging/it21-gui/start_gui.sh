#!/bin/bash
PATH="./dll:\$PATH"
export PATH
java -client ${javaOpts} -cp "./bin:./lib/*:./shared/*:./conf" ${mainProgramm}