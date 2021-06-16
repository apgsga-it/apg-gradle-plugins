package com.apgsga.maven

import org.apache.maven.model.Dependency
import java.io.InputStream
import java.util.*

/**
 *  Interface for retrieving and analyzing Dependency Management Information of a given pom.xml
 *  @author che
 */
interface MavenBomManager {

    /**
     * Of a pom  the DependencyManagement Section is loaded, analyzed and
     * collected into a List of [Dependency] 's
     * Artifact Coordinates:
     * @param bomGroupId Groupid of Bom Artifact
     * @param bomArtifactid ArtifactId of Bom
     * @param bomVersion Version of Bom
     * @param recursive if to resolve pom's recursively
     * @return List of [Dependency]
     */
    fun retrieve(bomGroupId: String, bomArtifactid: String, bomVersion: String, recursive: Boolean = true): Collection<Dependency>

    /**
     * @see [retrieve]
     * @param bomArtifact the following format groupId:artifactId:version
     * @return List of [Dependency]
     */
    fun retrieve(bomArtifact: String, recursive: Boolean = true): Collection<Dependency>


}


interface DependencyLoader {
    fun load(bomGroupId: String, bomArtifactid: String, bomVersion: String): InputStream
    fun load(mavenCoordinates: String): InputStream
}

