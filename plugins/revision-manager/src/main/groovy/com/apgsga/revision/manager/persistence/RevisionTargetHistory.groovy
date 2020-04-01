package com.apgsga.revision.manager.persistence

class RevisionTargetHistory {
    Map<String,Map<String,List<String>>> revisions

    RevisionTargetHistory(Map<String,Map<String,List<String>>> revisions) {
        this.revisions = revisions
    }

    RevisionTargetHistory() {
    }

    def add(String serviceName, String target, String versionPrefix, String revision) {
        if(!revisions.keySet().contains(serviceName)) {
            revisions.put(serviceName,new HashMap<String, List<String>>())
        }
        def targetHistory = revisions.get(serviceName).get(target) as List
        if (targetHistory == null) {
            targetHistory = new ArrayList<String>()
        }
        targetHistory.add("${versionPrefix}-${revision}".toString())
        revisions.get(serviceName)[target] = targetHistory
    }
}
