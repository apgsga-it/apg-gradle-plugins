#!/bin/bash
# post-uninstall.sh
#
echo "Post Uninstall script: \$1"
if [ "\$1" = "0" ]; then
	echo "Removing user ${targetServiceName}"
	/usr/sbin/userdel -r ${targetServiceName} 2> /dev/null || :
	echo "Removing group ${targetServiceName}"
	/usr/sbin/groupdel ${targetServiceName} 2> /dev/null || :
	echo "Removing ${targetServiceName} data directory: ${targetServiceDataDir}"
	rm -rf ${targetServiceDataDir}
	echo "Completely removing Installation directory ${targetServiceExecDir}"
	rm -rf ${targetServiceExecDir}
fi
exit 0