package com.apgsga.revision.manager.persistence

class RevisionTargetHistory {
    Map<String,Map<String,List<String>>> services

    RevisionTargetHistory(Map<String,Map<String,List<String>>> services) {
        this.services = services
    }

    RevisionTargetHistory() {
    }

    def add(String serviceName, String target, String versionPrefix, String revision) {
        if(!services.keySet().contains(serviceName)) {
            services.put(serviceName,new HashMap<String, List<String>>())
        }
        def targetHistory = services.get(serviceName).get(target) as List
        if (targetHistory == null) {
            targetHistory = new ArrayList<String>()
        }
        targetHistory.add("${versionPrefix}-${revision}".toString())
        services.get(serviceName)[target] = targetHistory
    }
}
