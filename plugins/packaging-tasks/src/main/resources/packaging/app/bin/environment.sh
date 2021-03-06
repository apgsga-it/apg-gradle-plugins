#!/bin/bash -x
#########################################################
# Generall Runtime Variable to make the scripts 
# easier to adopt to a specific target enviroment
export LD_LIBRARY_PATH=${targetServiceExecDir}/instantclient_11_2
export INSTALLATION_DIR=${targetServiceExecDir}
export CONFIG_DIR=${targetServiceConfigDir}
export DATA_DIR=${targetServiceDataDir}
export JAVA_DIR=${javaDir}
########################################################
# Java Settings
export JAVA_HOME=\$INSTALLATION_DIR/\$JAVA_DIR
JAVA_OPTS=-Djava.awt.headless=true
JAVA_OPTS="\$JAVA_OPTS -server"
JAVA_OPTS="\$JAVA_OPTS -Xms2048m"
JAVA_OPTS="\$JAVA_OPTS -Xmx10G"
JAVA_OPTS="\$JAVA_OPTS -Djavamelody.storage-directory=\$DATA_DIR";
export JAVA_OPTS;
#########################################################
# Adjust Path to installed Java Dist
PATH=\$JAVA_HOME/bin:\$PATH;export PATH
