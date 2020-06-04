#!/bin/bash

if [ $1 = "h" ] || [ $1 = "help" ]; then
  echo "Run the script without parameters and you'll be prompted for parameter. Or run the script with following parameters: <GitUserName> <GitClonePath> <MavenSettingFilePath> <mavenProfileToUse>"
  exit
fi

if [ "$#" -ne 4 ] && [ "$#" -gt 0 ]; then
  echo "Wrong number of parameter, run following to print help: './iniGradleProfile.sh -h'"
  exit
fi

if [ "$#" -eq 4 ]; then
  gitUsername=$1
  gitClonePath=$2
  mavenSettingPath=$3
  mavenProfile=$4
else
  echo "User to be used to clone from git.apgsga.ch:"
  read gitUsername

  echo "Where to clone the Git Repo:"
  read gitClonePath

  echo "Path to Maven settings.xml file:"
  read mavenSettingPath

  echo "Maven profile to be applied:"
  read mavenProfile
fi

echo "Git Repository will be clone, you'll be prompted for your password ..."
git clone "$gitUsername"@git.apgsga.ch:/var/git/repos/apg-gradle-properties.git "$gitClonePath"

sed -i '/apg.common.repo.maven.file.path=/d' "$gitClonePath"/gradle.properties
echo "apg.common.repo.maven.file.path=$mavenSettingPath" >> "$gitClonePath"/gradle.properties

sed -i '/apg.common.repo.gradle.maven.active.profile=/d' "$gitClonePath"/gradle.properties
echo "apg.common.repo.gradle.maven.active.profile=$mavenProfile" >> "$gitClonePath"/gradle.properties

echo "Gradle correctly initialized!"