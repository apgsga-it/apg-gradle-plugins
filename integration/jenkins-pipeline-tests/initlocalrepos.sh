#!/bin/bash
# Command line parsing
# Usage info
show_help() {
  cat <<EOF
Usage: ${0##*/} -r GITREPO] [-b BRANCH] [-i INSTALLDIR] -ns
Initializes Gradle User Home and MavenLocal for Jenkinsfile Runner based Tests
Gradle User Home is intialized from Apg's apg-gradle-properties git repo , with the according profiles
MavenLocal is initialized as a empty repo running maven and running compile dependency:resolve dependency:resolve-plugins
    -h          display this help and exit
    -u=USER     userid for the gitrepo, from which apg-gradle-properties will be clone
    -b=BRANCH   git of the git repo apg-gradle-properties
    -t=TARGETDIR Target Directory for MavenLocal & Gradle User Home

EOF
}
# saner programming env: these switches turn some bugs into errors
set -o errexit -o pipefail -o noclobber -o nounset
# -allow a command to fail with !’s side effect on errexit
# -use return value from ${PIPESTATUS[0]}, because ! hosed $?
! getopt --test >/dev/null
if [[ ${PIPESTATUS[0]} -ne 4 ]]; then
  echo "I’m sorry, $(getopt --test) failed in this environment, see if you install gnu-getopt "
  exit 1
fi

#Defaults
# Temp fix in Apg fork
REPO=git.apgsga.ch:/var/git/repos/apg-gradle-properties.git
BRANCH=master
TARGET_DIR="$HOME/jenkinstests"
MAVEN_DIR=maven
GRADLE_DIR=gradle
USER=



#Command line Options
OPTIONS=hu:r:b:t:
LONGOPTS=help,user:,branch:,targetdir:

# -regarding ! and PIPESTATUS see above
# -temporarily store output to be able to check for errors
# -activate quoting/enhanced mode (e.g. by writing out “--options”)
# -pass arguments only via   -- "$@"   to separate them correctly
! PARSED=$(getopt --options=$OPTIONS --longoptions=$LONGOPTS --name "$0" -- "$@")
if [[ ${PIPESTATUS[0]} -ne 0 ]]; then
  show_help
  exit 0
fi
# read getopt’s output this way to handle the quoting right:
eval set -- "$PARSED"
while true; do
  case "$1" in
  -h | --help)
    show_help
    exit 0
    ;;
  -u | --user)
    USER=$2
    shift 2
    ;;
  -b | --branch)
    BRANCH=$2
    shift 2
    ;;
  -t | --targetdir)
    TARGET_DIR=$2
    TARGET_DIR="${TARGET_DIR/#\~/$HOME}"
    shift 2
    ;;
  --)
    shift
    break
    ;;
  *)
    show_help >&2
    exit 1
    ;;
  esac
done
echo "Running with Target directory=$TARGET_DIR, repo=$REPO, user:$USER"
# Preconditions
mvn --version >/dev/null 2>&1 || {
  exit 1
}
git --version >/dev/null 2>&1 || {
  echo >&2 "git is either not in Path or Installed.  Aborting."
  exit 1
}
if [ ! -d "$TARGET_DIR" ]; then
  echo >&2 "Installation directtory $TARGET_DIR is missing.  Aborting."
  exit 1
fi
# Settings up maven
# Create Maven Directory
MAVEN_DIR_FULLPATH="$TARGET_DIR/maven"
if [ -d "$MAVEN_DIR_FULLPATH" ]; then
   echo "Deleteing Maven Target Dir:$MAVEN_DIR_FULLPATH recursively "
   rm -Rf "$MAVEN_DIR_FULLPATH"
fi
mkdir "$MAVEN_DIR_FULLPATH"
mkdir "$MAVEN_DIR_FULLPATH/repo"
echo "Copying and adopting settings.xml to $MAVEN_DIR_FULLPATH/settings.xml"
cat maven/settings.xml | sed s~#mavenlocal#~"$MAVEN_DIR_FULLPATH\/repo"~ > "$MAVEN_DIR_FULLPATH/settings.xml"
echo "Copying settings.xml done"
SAVEDWD=$(pwd)
echo "$SAVEDWD"
# Createing a initiall
export MAVEN_OPTS=-Dmaven.repo.local=$MAVEN_DIR_FULLPATH
cd "../modules/testapp-bom"
mvn install
cd "../testapp-parentpom"
mvn install
cd "../testapp-module"
mvn dependency:resolve dependency:resolve-plugins
