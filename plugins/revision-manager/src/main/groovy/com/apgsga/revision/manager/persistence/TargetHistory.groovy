package com.apgsga.revision.manager.persistence

class TargetHistory {
    Map<String,List<String>> revisions

    TargetHistory(Map<String,List<String>> revisions) {
        this.revisions = revisions
    }
    TargetHistory() {
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
