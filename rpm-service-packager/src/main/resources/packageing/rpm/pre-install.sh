#!/bin/bash
# pre-install.sh
#
# Stopping Service for update 

echo "Pre Install script: \$1"
if [ "\$1" = "2" ]; then
	echo "Stopping ${targetServiceName}"
	systemctl stop ${targetServiceName}
	echo "Stopping ${targetServiceName}-restart.service"
	systemctl stop ${targetServiceName}-restart.service
	echo "Stopping ${targetServiceName}-restart.timer"
	systemctl stop ${targetServiceName}-restart.timer
fi
if [ "\$1" = "1" ]; then
	mkdir ${targetServiceExecDir}
	echo "Creating group: ${targetServiceName}"
	/usr/sbin/groupadd -f -r ${targetServiceName} 2> /dev/null || :
	echo "Creating user: ${targetServiceName}"
	/usr/sbin/useradd -r -m -c "${targetServiceName} user" ${targetServiceName} -g ${targetServiceName} 2> /dev/null || :
	

fi

exit 0