#!/bin/bash
# pre-uninstall.sh
#
# Stopping Service for Uninstall 
echo "Pre Uninstall script: \$1"
if [ "\$1" = "0" ]; then
	echo "Stopping service ${targetServiceName}"
	systemctl stop ${targetServiceName}
	echo "Un-registering ${targetServiceName}"
	systemctl disable ${targetServiceName}
	echo "Resetting ${targetServiceName} from systemctl"
	systemctl reset-failed ${targetServiceName}
	echo "Stopping service ${targetServiceName}-restart.service"
	systemctl stop ${targetServiceName}-restart.timer
	echo "Un-registering ${targetServiceName}-restart.service"
	systemctl disable ${targetServiceName}-restart.timer
	echo "Stopping service ${targetServiceName}-restart.service"
	systemctl stop ${targetServiceName}-restart.service
	echo "Un-registering ${targetServiceName}-restart.service"
	systemctl disable ${targetServiceName}-restart.service
	
	
fi
exit 0