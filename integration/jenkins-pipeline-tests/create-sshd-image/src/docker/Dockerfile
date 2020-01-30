FROM centos:systemd
RUN yum -y update; yum clean all
RUN yum -y install openssh-server openssh-clients sudo passwd; yum clean all; systemclt enable sshd.servcice
ADD ./createUser.sh /createUser.sh
RUN mkdir /var/run/sshd
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key -N ''
RUN chmod 755 /createUser.sh
RUN ./createUser.sh

CMD ["/usr/sbin/init"]