package com.apgsga.maven.dm.tasks

import com.apgsga.maven.VersionResolver
import com.apgsga.maven.dm.ext.Bom
import com.apgsga.maven.dm.ext.VersionResolutionExtension
import com.apgsga.maven.dm.ext.version
import org.apache.maven.model.DependencyManagement
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Writer
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class CreateNewBomRevisionTask : AbstractVersionResolutionTask() {
    override fun doResolutionAction(versionResolver: VersionResolver, resolutionExtension: VersionResolutionExtension, bom: Bom) {
        val nextVersion = version(bom.baseVersion, resolutionExtension.bomNextRevision)
        // TODO (che, 28.2 ) : Possibly skip , when last and next Version same
        val bomModel = Model()
        bomModel.artifactId = bom.artifactId
        bomModel.groupId = bom.groupId
        bomModel.version = nextVersion
        // TODO (che, 28.2 ) : Better description?
        val date = LocalDateTime.now()
        val formatter =  DateTimeFormatter.ofPattern("dd-MMMM-yyyy hh:mm:ss")
        val dateFormatted = date.format(formatter)
        bomModel.description = "Generated by Gradle Task ${this.toString()} at $dateFormatted"
        bomModel.dependencyManagement = DependencyManagement()
        versionResolver.getMavenArtifactList()?.forEach {
            bomModel.dependencyManagement.addDependency(it)
        }
        val writer =  MavenXpp3Writer()
        val parentDir  = File(resolutionExtension.bomDestDirPath)
        parentDir.mkdirs()
        println ("Revision Parent Dir ${resolutionExtension.bomDestDirPath}")
        val bomFile = File(parentDir,"${bomModel.artifactId}-${bomModel.version}.pom")
        val fileWriter = FileWriter(bomFile)
        writer.write(fileWriter,bomModel)
        resolutionExtension.saveRevision()
    }


}