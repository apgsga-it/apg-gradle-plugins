FROM centos:centos7

RUN yum -y update; yum clean all
RUN yum -y install openssh-server openssh-clients passwd; yum clean all
ADD ./createUser.sh /createUser.sh
RUN mkdir /var/run/sshd

RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key -N ''

RUN chmod 755 /createUser.sh
EXPOSE 22
RUN ./createUser.sh
ENTRYPOINT ["/usr/sbin/sshd", "-D"]