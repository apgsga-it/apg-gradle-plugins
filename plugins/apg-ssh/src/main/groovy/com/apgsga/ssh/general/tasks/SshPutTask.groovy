package com.apgsga.ssh.general.tasks

import com.apgsga.ssh.common.AbstractSshTask
import org.gradle.api.tasks.Input

class SshPutTask extends AbstractSshTask {

    public static String TASK_NAME = "sshPut"

    @Input
    def from

    @Input
    def into

    @Override
    def doRun(Object remote, Object allowAnyHosts) {
        project.ssh.run {
            if (allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remote) {
                put from: from,  into: into
            }
        }
    }
}
