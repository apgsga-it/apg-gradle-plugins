#!/bin/bash
# functions used
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
    -m          Skip Maven Initialization
    -g          Skip Gradle Initialization

EOF
}
# Maven Setup
initMaven() {
  # Settings up maven
  # Create Maven Directory
  MAVEN_BASE_DIR="$TARGET_DIR/maven"
  if [ -d "$MAVEN_BASE_DIR" ]; then
    echo "Deleteing Maven Target Dir:$MAVEN_BASE_DIR recursively "
    rm -Rf "$MAVEN_BASE_DIR"
  fi
  mkdir "$MAVEN_BASE_DIR"
  mkdir "$MAVEN_BASE_DIR/repo"
  echo "Copying and adopting settings.xml to $MAVEN_BASE_DIR/settings.xml"
  cat maven/settings.xml | sed s~#mavenlocal#~"$MAVEN_BASE_DIR\/repo"~ >"$MAVEN_BASE_DIR/settings.xml"
  echo "Copying settings.xml done"
  SAVEDWD=$(pwd)
  echo "$SAVEDWD"
  # Createing a initially loaded Maven Repo
  echo "Filling an initial Maven Repo at $MAVEN_BASE_DIR/repo"
  export MAVEN_OPTS=-Dmaven.repo.local="$MAVEN_BASE_DIR/repo"
  mvn help:system
  cd "../modules/testapp-bom"
  mvn install
  cd "../testapp-parentpom"
  mvn install
  cd "../testapp-module"
  mvn dependency:resolve dependency:resolve-plugins
  cd "$SAVEDWD"
}
# Gradle Setup
initGradle() {
  # Createing a initially loaded Gradle User Home
  GRADLEHOMEDIR="$TARGET_DIR/gradle/home"
  if [ -d "$GRADLEHOMEDIR" ]; then
    echo "Deleteing Gradle Target Dir: $GRADLEHOMEDIR recursively "
    rm -Rf "$GRADLEHOMEDIR"
  fi
  mkdir -p "$TARGET_DIR/gradle/home"
  git clone "$USER@$GITREPO" "$GRADLEHOMEDIR"
  export GRADLE_USER_HOME=$GRADLEHOMEDIR
  ./gradlew --version
  ./gradlew tasks --group="Apg Gradle Jenkinsrunner"
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
GITREPO="git.apgsga.ch:/var/git/repos/apg-gradle-properties.git"
BRANCH=master
TARGET_DIR="$HOME/jenkinstests"
MAVEN_DIR=maven
GRADLE_DIR=gradle
USER=
MAVEN=y
GRADLE=y

#Command line Options
OPTIONS=hu:r:b:t:mg
LONGOPTS=help,user:,branch:,targetdir:,nomaven,nogradle

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
  -m | --nomaven)
    MAVEN=n
    shift
    ;;
 -g | --nogradle)
    GRADLE=n
    shift
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
echo "Running with Target directory=$TARGET_DIR, repo=$GITREPO, user:$USER"
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
if [ ! -d "$TARGET_DIR" ]; then
  echo >&2 "Installation directtory $TARGET_DIR is missing.  Aborting."
  exit 1
fi
if [ -z "$USER" ]; then
  echo >&2 "User Parameter not set.  Aborting."
  exit 1
fi
if [ $MAVEN == "y" ]; then
  initMaven
fi
if [ $GRADLE == "y" ]; then
  initGradle
fi