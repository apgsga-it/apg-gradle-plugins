package com.apgsga.maven

data class MavenArtifact(val groupId: String, val artifactid: String, val version: String, val type: String)
/**
 *  Interface for retrieving and analyzing Dependency Management Information of a given pom.xml
 *  @author che
 */
interface MavenBomManager {

    /**
     * Of a given pom  the DependencyManagement Section is loaded, analyzed and
     * collected into a List of
     * Artifact Coordinates:
     * @param groupId Groupid of Artifact
     * @param artifactid ArtifactId
     * @param version Version
     * @return List of [MavenArtifact]
     */
    fun retrieve(groupId: String, artifactid: String, version: String, recursive: Boolean = true): Collection<MavenArtifact>

    /**
     * @see [retrieve]
     * @param artifact the following format groupId:artifactId:version
     * @return List of [MavenArtifact]
     */
    fun retrieve(artifact: String, recursive: Boolean = true): Collection<MavenArtifact>

}

