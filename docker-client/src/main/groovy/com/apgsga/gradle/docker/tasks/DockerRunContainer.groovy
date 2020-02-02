package com.apgsga.gradle.docker.tasks

import com.bmuschko.gradle.docker.tasks.AbstractDockerRemoteApiTask
import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.api.model.Ports
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

class DockerRunContainer extends AbstractDockerRemoteApiTask {

    @Input
    final Property<String> imageName = project.objects.property(String)

    @Input
    final Property<String> portBinding = project.objects.property(String)

    @Input
    final Property<String> networkName = project.objects.property(String)

    @Input
    final Property<String> containerName = project.objects.property(String)

    @Input
    final Property<Boolean> remove = project.objects.property(Boolean)

    @Input
    final Property<Boolean> privileged = project.objects.property(Boolean)

    @Input
    final Property<List<String>> commands = project.objects.property(List<String>)




    @Override
    void runRemoteCommand()  {
        runRemoteCommand(dockerClient)
    }
    void runRemoteCommand(def dockerClient) {
        String[] ports = portBinding.get().split(":")
        Ports portBindings = new Ports()
        portBindings.bind(Ports.Binding(Integer.parseInt(ports[0])), Ports.Binding(Integer.parseInt(ports[1])))
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(imageName.get())
        def network = networkName.getOrNull()
        if (network != null) {
            containerCmd.with
        }
        CreateContainerResponse container = containerCmd
                .withCmd("true")
                .withPortBindings(portBindings)
                .exec()

        dockerClient.startContainerCmd(container.getId()).exec()
    }
}
