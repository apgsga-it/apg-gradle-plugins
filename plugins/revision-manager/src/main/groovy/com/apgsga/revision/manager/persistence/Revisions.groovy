package com.apgsga.revision.manager.persistence

class Revisions  {
    String currentRevision
    Map<String,Map<String, String>> services

    Revisions(String currentRevision, Map<String,Map<String, String>> services) {
        this.currentRevision = currentRevision
        this.services = services
    }

    Revisions() {
    }

    def lastRevision(String serviceName, String target, String revision) {
        services[serviceName][target] = revision
    }

}
