FROM debian:stretch

RUN apt-get update && \
apt-get install -y openssh-client openssh-server sudo && \
mkdir /var/run/sshd

ADD ./createUser.sh /createUser.sh

RUN chmod 755 /createUser.sh
EXPOSE 22
RUN ./createUser.sh


ENTRYPOINT ["/usr/sbin/sshd", "-D"]
