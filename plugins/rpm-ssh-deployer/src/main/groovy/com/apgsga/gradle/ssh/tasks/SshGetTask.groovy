package com.apgsga.gradle.ssh.tasks

import org.gradle.api.tasks.Input
// TODO (che, jhe , 8.3) : we should better
class SshGetTask extends AbstractSshTask {

    @Input
    def from
    @Input
    def into

    @Override
    def doRun(remotes,allowAnyHosts) {
        project.ssh.run {
            if (allowAnyHosts) {
                project.logger.info("Allowing SSH Anyhosts ")
                settings {
                    knownHosts = allowAnyHosts
                }
            }
            session(remotes) {
                get from: from,  into: into
            }
        }
    }

}