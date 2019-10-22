#!/bin/bash
# post-install.sh
#
echo "Post Install script: \$1"

echo "Setting permissions on ${targetServiceExecDir}"
chown -R ${targetServiceName}:${targetServiceName} ${targetServiceExecDir}

if [ "\$1" = "1" ]; then
	
	echo "Installation and configuration of Java Environment started ..."
	WORK_DIR=`mktemp -d -p  ${targetServiceExecDir}`
	echo "Created temp dir \$WORK_DIR"
	curl --output \$WORK_DIR/${javaDist} -L ${distRepoUrl}/${javaDist}
	tar xvf \$WORK_DIR/${javaDist} -C ${targetServiceExecDir}
	rm -rf \$WORK_DIR
	echo "Deleted temp working directory \$WORK_DIR"
	echo "Installation and configuration of Java Environment done."
	
	echo "Registering ${targetServiceName} for Boot time start"
	systemctl enable ${targetServiceName}
	echo "Starting ${targetServiceName}"
	systemctl start ${targetServiceName}
	echo "Registering ${targetServiceName}-restart.service for Boot time start"
	systemctl enable ${targetServiceName}-restart.service
	echo "Starting ${targetServiceName}-restart.service"
	systemctl start ${targetServiceName}-restart.service
	echo "Registering ${targetServiceName}-restart.timer for Boot time start"
	systemctl enable ${targetServiceName}-restart.timer
	echo "Starting ${targetServiceName}-restart.timer"
	systemctl start ${targetServiceName}-restart.timer
	
	
fi
if [ "\$1" = "2" ]; then
	echo "Starting ${targetServiceName}"
	systemctl start ${targetServiceName}
	echo "Starting ${targetServiceName}-restart.service"
	systemctl start ${targetServiceName}-restart.service
	echo "Starting ${targetServiceName}-restart.timer"
	systemctl start ${targetServiceName}-restart.timer
fi
exit 0