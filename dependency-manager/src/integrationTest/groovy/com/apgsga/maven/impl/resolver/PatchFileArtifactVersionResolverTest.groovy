package com.apgsga.maven.impl.resolver


import spock.lang.Shared
import spock.lang.Specification

class PatchFileArtifactVersionResolverTest extends Specification {


   @Shared
   def source = new File("src/integrationTest/resources/patchresolvertests")


    def "getVersion from Patchfile"() {
        given:
        def patchFile = new File(source,"PatchZ5401.json")
        def recommender = new PatchFileArtifactVersionResolver(patchFile)
        when:
        def version = recommender.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"

    }

    def "getVersion Simple Test with nested Bom no recursive"() {
        given:
        def patchFile = new File(source,"PatchZ5401.json")
        def recommender = new PatchFileArtifactVersionResolver(patchFile)
        when:
        def version = recommender.getVersion("com.affichage.it21.vk","xxxxxx-dao")
        then:
        version == ""

    }
}
