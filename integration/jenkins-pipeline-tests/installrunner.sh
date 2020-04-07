#!/bin/bash
TARGET_DIR=~/git/jenkinsfile-runner
# TODO (che, jhe, .4) : Master broken
#REPO=https://github.com/jenkinsci/jenkinsfile-runner.git
# Temp fix in Apg fork
REPO=https://github.com/apgsga-it/jenkinsfile-runner.git
BRANCH=master
RUNNER_DIR=runner
BIN_DIR=bin
JENKINS_DIR=jenkins
# Preconditions
mvn --version >/dev/null 2>&1 || {
  exit 1
}
git --version >/dev/null 2>&1 || {
  echo >&2 "git is either not in Path or Installed.  Aborting."
  exit 1
}
if [ ! -d $RUNNER_DIR ]; then
  echo >&2 "Installation directtory $RUNNER_DIR for jenkinsfile-runner missing.  Aborting."
  exit 1
fi
SAVEDWD=$(pwd)
echo "$SAVEDWD"
# Target Directory
if [ -d $TARGET_DIR ]; then
  echo "The Target Directory for the jenkinsfile-runner exists, delete it and then clone the repo? Y/n"
  read varname
  if [ "$varname" = "Y" ]; then
    echo "$TARGET_DIR will be deleted recursively "
    rm -Rf $TARGET_DIR
  else
    echo "Continueing "
  fi
fi
echo "Cloneing jenkinsfile-runner from $REPO to $TARGET_DIR from branch: $BRANCH"
git clone -b $BRANCH $REPO $TARGET_DIR
echo "Done."
echo "Building jenkinsfile-runner "
cd "$TARGET_DIR"
pwd
mvn clean package
cd "$SAVEDWD" || {
  echo >&2 "Could'nt cd to $SAVEDWD.  Aborting."
  exit 1
}
pwd
cd $RUNNER_DIR || {
  echo >&2 "Could'nt cd to $RUNNER_DIR.  Aborting."
  exit 1
}
if [ -d $BIN_DIR ]; then
  echo "Deleting Target bin directory $SAVEDWD/$BIN_DIR"
  rm -Rf $BIN_DIR
  echo "Done"
fi
if [ -d $JENKINS_DIR ]; then
  echo "Deleting jenkins directory  $SAVEDWD/$JENKINS_DIR"
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
./gradlew runTestLibHelloWorld --info --stacktrace
