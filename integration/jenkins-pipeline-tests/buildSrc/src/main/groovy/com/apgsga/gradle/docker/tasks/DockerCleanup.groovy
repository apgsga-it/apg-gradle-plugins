package com.apgsga.gradle.docker.tasks

import com.bmuschko.gradle.docker.tasks.AbstractDockerRemoteApiTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

class DockerCleanup extends AbstractDockerRemoteApiTask {

    @Input
    final Property<String> containerNames = project.objects.property(String)

    @Input
    final Property<String> networkNames = project.objects.property(String)

    @Override
    void runRemoteCommand()  {
        runRemoteCommand(dockerClient)
    }
    void runRemoteCommand(def dockerClient) {
        def containersList = containerNames.get().split(":") as Collection
        project.logger.info("Container Filter: ${containersList.toString()}")
        def containers = dockerClient.listContainersCmd()
                .withNameFilter(containersList)
                .exec()
        for (container in containers) {
            project.logger.info("Stopping constainer: ${container.toString()} with Id: ${container.getId()} ")
            dockerClient.stopContainerCmd(container.getId()).exec()
            project.logger.info("Stopped constainer: ${container.toString()}")
        }
        def networksList = networkNames.get().split(":")
        project.logger.info("Network Filter: ${networksList.toString()}")
        def networks = dockerClient.listNetworksCmd()
                .withNameFilter(networksList)
                .exec()
        for (network in networks) {
            project.logger.info("Removing network: ${network.toString()} with Id:  ${network.getId()}")
            dockerClient.removeNetworkCmd(network.getId()).exec()
        }

    }
}
