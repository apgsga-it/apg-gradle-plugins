# apg-gradle-properties
This repository provides the properties files required by APG-Gradle-Plugins. Content of this repository has to be checked-out into the GRADLE_USER_HOME folder.

That means, if GRADLE_USER_HOME refers to /var/opt/gradle/home, one will have to run the following command:

* git clone https://github.com/apgsga-it/apg-gradle-properties.git /var/opt/gradle/home

Note that the GRADLE_USER_HOME folder has to be empty before cloning. If not empty, then first delete all its content.

As soon as the check-out has been done, one will have to configure the credentials using the "nu.studer.credentials" Plugin: https://github.com/etiennestuder/gradle-credentials-plugin

The following keys have to be added:

* devUser
* devPw
* deployUser
* deployPw

In other words, one need to run the following commands:

* gradle addCredential --key devUser --value \<xxxx\> -Dgradle.user.home=\<PathToGradleUserHome\>
* gradle addCredential --key devPw --value \<xxxx\> -Dgradle.user.home=\<PathToGradleUserHome\>
* gradle addCredential --key deployUser --value \<xxxx\> -Dgradle.user.home=\<PathToGradleUserHome\>
* gradle addCredential --key deployPw --value \<xxxx\> -Dgradle.user.home=\<PathToGradleUserHome\>

Note that if there is no "gradle" available in the CLASSPATH, one can also run the above commands from within any Gradle Project where a wrapper is available. Simply replace "gradle" with "gradlew". The above commands won't affect the project.

When finished, a file called gradle.encrypted.properties should exists within the GRADLE_USER_HOME folder.