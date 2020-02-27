package com.apgsga.revision.manager.persistence

class RevisionTargetHistory {
    Map<String,List<String>> revisions

    RevisionTargetHistory(Map<String,List<String>> revisions) {
        this.revisions = revisions
    }

    RevisionTargetHistory() {
    }

    def add(String target, versionPrefix, revision) {
        def targetHistory = revisions[target as String] as List
        if (targetHistory == null) {
            targetHistory = new ArrayList<String>()
        }
        targetHistory.add("${versionPrefix}-${revision}".toString())
        revisions[target as String] = targetHistory
    }
}
