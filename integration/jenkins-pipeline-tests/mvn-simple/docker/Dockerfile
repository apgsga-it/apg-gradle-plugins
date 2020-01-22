FROM jenkins4eval/jenkinsfile-runner

# TODO (che, 22.1 ) nested from images probably the better approach here
# Maven
# Downloading and installing Maven
ARG MAVEN_VERSION=3.5.4

# 1- Define a constant with the working directory
ARG USER_HOME_DIR="/root"

# 2- Define the URL where maven can be downloaded from
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

# 3 - Create the directories, download maven, validate the download, install it, remove downloaded file and set links
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && echo "Downlaoding maven" \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  \
  && echo "Unziping maven" \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  \
  && echo "Cleaning and setting links" \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# 4 - Define environmental variables required by Maven, like Maven_Home directory and where the maven repo is located
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# Gradle
# Downloading and installing Gradle
# 5- Define a constant with the version of gradle you want to install
ARG GRADLE_VERSION=5.6.3
# 6- Define the URL where gradle can be downloaded from
ARG GRADLE_BASE_URL=https://services.gradle.org/distributions

# 7- Create the directories, download gradle, validate the download, install it, remove downloaded file and set links
RUN mkdir -p /usr/share/gradle /usr/share/gradle/ref \
  && echo "Downlaoding gradle" \
  && curl -fsSL -o /tmp/gradle.zip ${GRADLE_BASE_URL}/gradle-${GRADLE_VERSION}-bin.zip \
  \
  && echo "Unziping gradle" \
  && unzip -d /usr/share/gradle /tmp/gradle.zip \
   \
  && echo "Cleaning and setting links" \
  && rm -f /tmp/gradle.zip \
  && ln -s /usr/share/gradle/gradle-${GRADLE_VERSION} /usr/bin/gradle

# 85- Define environmental variables required by gradle
ENV GRADLE_VERSION 5.6.3
ENV GRADLE_HOME /usr/bin/gradle
ENV GRADLE_USER_HOME /gradle
ENV PATH $PATH:$GRADLE_HOME/bin

COPY modules /workspace/
COPY repo /maven/repo/
COPY settings.xml "$MAVEN_CONFIG"/settings.xml
COPY gradle.encrypted.properties "$GRADLE_USER_HOME"/gradle.encrypted.properties
COPY gradle.properties "$GRADLE_USER_HOME"/gradle.properties
COPY repoNames.json "$GRADLE_USER_HOME"/repoNames.json
COPY supportedServices.json "$GRADLE_USER_HOME"/supportedServices.json

ENTRYPOINT ["/app/bin/jenkinsfile-runner", \
            "-w", "/app/jenkins",\
            "-p", "/usr/share/jenkins/ref/plugins",\
            "-f", "/workspace"]