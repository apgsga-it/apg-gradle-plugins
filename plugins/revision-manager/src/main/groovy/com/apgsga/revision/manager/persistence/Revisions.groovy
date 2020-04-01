package com.apgsga.revision.manager.persistence

class Revisions  {
    String currentRevision
    Map<String,Map<String, String>> targets

    Revisions(String currentRevision, Map<String,Map<String, String>> targets) {
        this.currentRevision = currentRevision
        this.targets = targets
    }

    Revisions() {
    }

    def lastRevision(String serviceName, String target, String revision) {
        targets[serviceName][target] = revision
    }

}
