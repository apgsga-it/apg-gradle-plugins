#!/bin/bash -x
source ${targetServiceExecDir}/bin/environment.sh
exec \$JAVA_HOME/jre/bin/java \$JAVA_OPTS -cp ".:\$INSTALLATION_DIR/lib/*:\$CONFIG_DIR" ${mainPrg} --spring.config.name=appconfig --spring.config.location=classpath:app/,classpath:/,classpath:ops/resource.properties
