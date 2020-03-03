package com.apgsga.revision.manager.persistence

class Revisions  {
    Integer currentRevision
    Map<String, String> targets

    Revisions(Integer currentRevision, Map<String, String> targets) {
        this.currentRevision = currentRevision
        this.targets = targets
    }

    Revisions() {
    }

    def lastRevision(String target, String revision) {
        targets[target] = revision
    }

}
