package com.apgsga.maven.impl.resolver


import spock.lang.Shared
import spock.lang.Specification

class PatchFileArtifactVersionResolverTest extends Specification {


   @Shared
   def source = new File("src/integrationTest/resources/patchresolvertests")

/*
    def "getVersion from Patchfile"() {
        given:
        def recommender = ResolverBuilderKt.create(PatchFileVersionResolverBuilder.class)
                .parentDir(source)
                .patchFile("PatchZ5401.json")
                .build()
        when:
        def version = recommender.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"

    }
*/
    /*
    def "getVersion Simple Test with nested Bom no recursive"() {
        given:
        def recommender = ResolverBuilderKt.create(PatchFileVersionResolverBuilder.class)
                .parentDir(source)
                .patchFile("PatchZ5401.json")
                .build()
        when:
        def version = recommender.getVersion("com.affichage.it21.vk","xxxxxx-dao")
        then:
        version == ""

    }

     */
}
