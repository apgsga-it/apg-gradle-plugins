package com.apgsga.packaging.util

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.springframework.core.io.FileSystemResourceLoader

class CopyUtil {
    static copy(Class clx,Project project, Copy copy) {
        def resource = clx.getProtectionDomain().getCodeSource().getLocation().toExternalForm()
        if (resource.endsWith("jar") && !project.rootProject.name.startsWith("gradletestproject")) {
            copy.from(project.zipTree(resource).matching {
                include 'packaging/**'
            })
            copy.into("${project.buildDir}")
        } else {
            def rl = new FileSystemResourceLoader()
            def rs = rl.getResource("classpath:packaging").getFile()
            copy.from(project.fileTree(rs).matching {
                include '**/*.*'
            })
            copy.into("${project.buildDir}/packaging")
        }
    }
}
