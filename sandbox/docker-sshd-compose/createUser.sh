#!/bin/bash

__create_user() {
# Create a user to SSH into as.
useradd tu1
SSH_USERPASS=tu1_pass
echo -e "$SSH_USERPASS\n$SSH_USERPASS" | (passwd --stdin tu1)
echo ssh tu1 password: $SSH_USERPASS
}

# Call all functions
__create_user