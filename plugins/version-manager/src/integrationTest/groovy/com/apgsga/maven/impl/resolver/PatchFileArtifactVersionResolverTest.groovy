package com.apgsga.maven.impl.resolver

import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

// TODO (che , jhe , 8.9: these tests need to move to functionaltests , since for resolving boms the Gradle proper Resolver is used"

class PatchFileArtifactVersionResolverTest extends Specification {


   @Shared
   def source = new File("src/integrationTest/resources/patchresolvertests")

    @Ignore()
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

    @Ignore()
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

}
