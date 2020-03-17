package com.apgsga.ssh.common


import org.gradle.api.Project

class SshGenericTask {

    private boolean isSudo = false

    private String sshCmd

    private boolean allowAnyHost = false

    private Object remote

    private boolean pty = true

    private printResult = false

    private Project project

    private SshGenericTask(){}

    static SshGenericTask create() {
        return new SshGenericTask()
    }

    SshGenericTask isSudo(boolean isSudo) {
        this.isSudo = isSudo
        this
    }

    SshGenericTask sshCmd(String sshCmd) {
        this.sshCmd = sshCmd
        this
    }

    SshGenericTask allowAnyHost(Boolean allowAnyHost) {
        this.allowAnyHost = allowAnyHost
        this
    }

    SshGenericTask remote(Object remote) {
        this.remote = remote
        this
    }

    SshGenericTask pty(boolean pty) {
        this.pty = pty
        this
    }

    SshGenericTask printResult(boolean printResult) {
        this.printResult = printResult
        this
    }

    SshGenericTask project(Project project) {
        this.project = project
        this
    }

    def doRun() {
        project.ssh.run() {
            if (allowAnyHost) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHost
                }
            }
            session(remote) {
                if(isSudo) {
                    executeSudo (sshCmd, pty: pty) {r ->
                        // JHE: not sure we need/want this, but for now this helps us when testing ...
                        if (printResult)
                            println r
                    }
                }
                else {
                    execute (sshCmd, pty: pty) {r ->
                        // JHE: not sure we need/want this, but for now this helps us when testing ...
                        if(printResult)
                            println r
                    }
                }
            }
        }
    }
}
