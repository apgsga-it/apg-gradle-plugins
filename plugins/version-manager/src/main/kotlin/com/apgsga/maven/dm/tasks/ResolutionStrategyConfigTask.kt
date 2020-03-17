package com.apgsga.maven.dm.tasks

import com.apgsga.maven.VersionResolver
import com.apgsga.maven.dm.ext.VersionResolutionExtension

open class ResolutionStrategyConfigTask : AbstractVersionResolutionTask() {

    override fun doResolutionAction(versionResolver: VersionResolver,resolutionExtension: VersionResolutionExtension) {
        val config = project.configurations.findByName(resolutionExtension.configurationName)
        config?.resolutionStrategy?.eachDependency {
            // TODO (che, jhe , 27.9 ) : Needs to be discussed, basically apg version resolution takes precedence over specified version
            var versionUsed = versionResolver.getVersion(requested.group, requested.name)
            if (versionUsed.isEmpty() && !requested.version.isNullOrEmpty() ) versionUsed = requested.version!!
            this.useVersion(versionUsed)
            this.because("Apg Version Resolver ")
        }
    }



}