#!/bin/bash

__create_user() {
  # Create a user to SSH into as.
  useradd -m -s /bin/bash tu1
  export SSH_USERPASS=tu1_pass
  echo -e "$SSH_USERPASS\n$SSH_USERPASS" | passwd tu1
  echo ssh tu1 password: $SSH_USERPASS
  usermod -aG sudo tu1
}
# Call all functions
__create_user
