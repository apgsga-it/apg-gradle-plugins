FROM jenkins4eval/jenkinsfile-runner
# TODO (che, 22.1 ) nested from images could maybe the better approach here

ARG USER_HOME_DIR="/root"

# Maven / Gradle Versions
ARG MAVEN_VERSION=3.5.4
ARG GRADLE_VERSION=5.6.3

# Maven Install
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

# Create the directories, download maven, validate the download, install it, remove downloaded file and set links
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

# Define environmental variables required by Maven, like Maven_Home directory and where the maven repo is located
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
# Mavenlocal Volumne
VOLUME mavenLocal

# Gradle Install
ARG GRADLE_BASE_URL=https://services.gradle.org/distributions

# Create the directories, download gradle, validate the download, install it, remove downloaded file and set links
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

# Define environmental variables required by gradle
VOLUME /gradleUserHome
ENV GRADLE_VERSION 5.6.3
ENV GRADLE_HOME /usr/bin/gradle
ENV GRADLE_USER_HOME /gradleUserHome
ENV PATH $PATH:$GRADLE_HOME/bin
#
# Copy Modules to Test and settings.xml

COPY settings.xml "$MAVEN_CONFIG"/settings.xml

ENTRYPOINT ["/app/bin/jenkinsfile-runner", \
            "-u", \
            "-w", "/app/jenkins",\
            "-p", "/usr/share/jenkins/ref/plugins",\
            "-f", "/workspace"]
CMD ["sleep", "infinity"]