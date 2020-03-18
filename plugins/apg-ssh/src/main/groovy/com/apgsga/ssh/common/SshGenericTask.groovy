package com.apgsga.ssh.common

import org.gradle.api.tasks.Input

class SshGenericTask extends AbstractSshTask {

    public static String TASK_NAME = "sshGenericTask"

    @Input
    Cmd cmd

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        project.ssh.run() {
            if (allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remote) {
                if(cmd.isSudo) {
                    executeSudo (cmd.sshCmd, pty: cmd.pty) {r ->
                        // JHE: not sure we need/want this, but for now this helps us when testing ...
                        if (cmd.printResult)
                            println r
                    }
                }
                else {
                    execute (cmd.sshCmd, pty: cmd.pty) {r ->
                        // JHE: not sure we need/want this, but for now this helps us when testing ...
                        if(cmd.printResult)
                            println r
                    }
                }
            }
        }
    }
}
