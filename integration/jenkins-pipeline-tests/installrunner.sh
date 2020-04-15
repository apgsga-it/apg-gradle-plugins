#!/bin/bash
# Command line parsing
# Usage info
show_help() {
  cat <<EOF
Usage: ${0##*/} [-d BUILDDIR] [-r GITREPO] [-b BRANCH] [-i INSTALLDIR] -ns
Builds and Installs the jenkinsfile-runner to a installation Dir to be used with the Jenkinspipeline Tests

    -h          display this help and exit
    -d=BUILDDIR target of the git clone of the git repo
    -r=GITREPO  git repo, from which the jenkinsfile runner will be cloned
    -b=BRANCH   git of the git repo
    -i=INSTALLDIR Installation Dir of the jenkinsfile runner
    -m=MAVENBASEDIR alternative Mavenlocal Base Directory, expect maven directory as child and a settings.xml
    -n          do not delete and clone the Builddir, if it exists
    -s          skip maven package of jenkinsfile-runner

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
TARGET_DIR=~/git/jenkinsfile-runner
# TODO (che, jhe, .4) : Master broken
#REPO=https://github.com/jenkinsci/jenkinsfile-runner.git
# Temp fix in Apg fork
REPO=https://github.com/apgsga-it/jenkinsfile-runner.git
BRANCH=master
RUNNER_DIR="$HOME/jenkinstests/runner"
BIN_DIR=bin
JENKINS_DIR=jenkins
CLEAN=Y
SKIP=n
# TODO (che,14.2) : see if only of the following could be possible , mavensettings.
MAVENBASEDIR=

#Command line Options
OPTIONS=hd:r:b:i:m:l:ns
LONGOPTS=help,builddir:,repo:,branch:,installdir:,mavenLocal:,mavenSettings:,noclean,skip

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
  -d | --builddir)
    TARGET_DIR=$2
    TARGET_DIR="${TARGET_DIR/#\~/$HOME}"
    shift 2
    ;;
  -r | --repo)
    REPO=y
    shift 2
    ;;
  -b | --branch)
    BRANCH=$2
    shift 2
    ;;
  -i | --installdir)
    RUNNER_DIR=$2
    RUNNER_DIR="${RUNNER_DIR/#\~/$HOME}"
    shift 2
    ;;
  -m | --mavenLocal)
    MAVENBASEDIR=$2
    MAVENBASEDIR="${MAVENBASEDIR/#\~/$HOME}"
    shift 2
    ;;
  -n | --noclean)
    CLEAN=n
    shift
    ;;
  -s | --skip)
    SKIP=Y
    shift
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
echo "Running with builddir=$TARGET_DIR, repo=$REPO, branch:$BRANCH, installdir=$RUNNER_DIR, clean=$CLEAN"
# Preconditions
mvn --version >/dev/null 2>&1 || {
  echo >&2 "mvn is either not in Path or Installed.  Aborting."
  exit 1
}
git --version >/dev/null 2>&1 || {
  echo >&2 "git is either not in Path or Installed.  Aborting."
  exit 1
}
if [ ! -d $RUNNER_DIR ]; then
  echo >&2 "Installation directtory $RUNNER_DIR for jenkinsfile-runner is missing.  Aborting."
  exit 1
fi
if [ ! -z "$MAVENBASEDIR" ]; then
  if [ ! -d "$MAVENBASEDIR" ]; then
    echo >&2 "Maven base  directory is missing for  $MAVENBASEDIR.  Aborting."
    exit 1
  fi
  if [ ! -d "$MAVENBASEDIR/repo" ]; then
    echo >&2 "Maven repo  directory is missing for  $MAVENBASEDIR.  Aborting."
    exit 1
  fi
  if [ ! -f "$MAVENBASEDIR/settings.xml" ]; then
    echo >&2 "Maven settings.xml file is missing for  $MAVENBASEDIR.  Aborting."
    exit 1
  fi
fi
SAVEDWD=$(pwd)
echo "$SAVEDWD"
# Target Directory
if [ -d "$TARGET_DIR" ] && [ $CLEAN == "Y" ]; then
  echo "$TARGET_DIR will be deleted recursively "
  rm -Rf $TARGET_DIR
fi
if [ ! -d "$TARGET_DIR" ]; then
  echo "Cloneing jenkinsfile-runner from $REPO to $TARGET_DIR from branch: $BRANCH"
  git clone -b $BRANCH $REPO $TARGET_DIR
  echo "Done."
  echo "Building jenkinsfile-runner "
else
  echo "Skipping git clone, because directory already there and clean=$CLEAN"
fi
cd "$TARGET_DIR"
pwd
if [ $SKIP == 'n' ]; then
  mvn clean package
fi
cd "$SAVEDWD" || {
  echo >&2 "Could'nt cd to $SAVEDWD.  Aborting."
  exit 1
}
pwd
cd $RUNNER_DIR || {
  echo >&2 "Could'nt cd to $RUNNER_DIR.  Aborting."
  exit 1
}
pwd
if [ -d $BIN_DIR ]; then
  echo "Deleting Target bin directory $RUNNER_DIR/$BIN_DIR"
  rm -Rf $BIN_DIR
  echo "Done"
fi
if [ -d $JENKINS_DIR ]; then
  echo "Deleting jenkins directory  $RUNNER_DIR/$JENKINS_DIR"
  rm -Rf $JENKINS_DIR
  echo "Done"
fi
mkdir $JENKINS_DIR
mkdir $BIN_DIR
cp "$TARGET_DIR/app/target/jenkinsfile-runner-standalone.jar" "$BIN_DIR"
cp -r "$TARGET_DIR/vanilla-package/target/war" "$JENKINS_DIR"
cp -r "$TARGET_DIR/vanilla-package/target/plugins" "$JENKINS_DIR"
echo "Installation of jenkinsfile-runner done"
echo "Testing installation by executing Gradle Test Build"
cd "$SAVEDWD" || {
  echo >&2 "Could'nt cd to $SAVEDWD.  Aborting."
  exit 1
}
./gradlew tasks --group="Apg Gradle Jenkinsrunner"
if [[ -z "$MAVENBASEDIR" ]]; then
  ./gradlew runTestLibHelloWorld -PinstallDir="$RUNNER_DIR" -PmavenSettings="$MAVENBASEDIR/settings.xml" --info --stacktrace
else
  ./gradlew runTestLibHelloWorld -PinstallDir="$RUNNER_DIR" --info --stacktrace
fi
