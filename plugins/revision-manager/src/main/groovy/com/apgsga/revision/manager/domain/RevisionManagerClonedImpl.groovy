package com.apgsga.revision.manager.domain

class RevisionManagerClonedImpl implements RevisionManager{

    private RevisionManager delegate

    RevisionManagerClonedImpl(RevisionManager delegate) {
        this.delegate = delegate
    }

    @Override
    void saveRevision(String serviceName, String target, String revision, String fullRevisionPrefix) {
        throw new RuntimeException("saveRevision not supported by ${RevisionManagerClonedImpl.class.name}")
    }

    @Override
    String lastRevision(String serviceName, String target) {
       delegate.lastRevision(serviceName,target)
    }

    @Override
    String nextRevision() {
        throw new RuntimeException("nextRevision not supported by ${RevisionManagerClonedImpl.class.name}")
    }
}
