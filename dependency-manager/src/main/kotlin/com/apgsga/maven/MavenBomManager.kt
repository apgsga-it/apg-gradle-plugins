package com.apgsga.maven

import java.util.*

data class MavenArtifact(val groupId: String, val artifactid: String, val version: String, val type: String)
/**
 *  Interface for retrieving and analyzing Dependency Management Information of a given pom.xml
 *  @author che
 */
interface MavenBomManager {

    /**
     * Of a pom  the DependencyManagement Section is loaded, analyzed and
     * collected into a List of [MavenArtifact] 's
     * Artifact Coordinates:
     * @param groupId Groupid of Bom Artifact
     * @param artifactid ArtifactId of Bom
     * @param version Version of Bom
     * @param recursive if to resolve pom's recursively
     * @return List of [MavenArtifact]
     */
    fun retrieve(groupId: String, artifactid: String, version: String, recursive: Boolean = true): Collection<MavenArtifact>

    /**
     * @see [retrieve]
     * @param bomArtifact the following format groupId:artifactId:version
     * @return List of [MavenArtifact]
     */
    fun retrieve(bomArtifact: String, recursive: Boolean = true): Collection<MavenArtifact>


    /**
     * @see [retrieve]
     * @return as [Properties] from Collection of [MavenArtifact] 's
     */
    fun retrieveAsProperties(bomArtifact: String, recursive: Boolean = true) : Properties

    /**
     *  Calculates the Intersection of [MavenArtifact]'s of two pom DependencyManagement Sections
     *
     * @param firstBomArtifact
     * @param recursive
     * @return Intersection of [MavenArtifact] as List
     */
    fun intersect(firstBomArtifact: String, secondBomArtifact: String, recursive: Boolean = false): Collection<MavenArtifact>

}

