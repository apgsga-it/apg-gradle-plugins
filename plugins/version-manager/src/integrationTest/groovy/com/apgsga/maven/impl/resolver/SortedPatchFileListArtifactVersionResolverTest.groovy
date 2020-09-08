package com.apgsga.maven.impl.resolver

import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
// TODO (che , jhe , 8.9: these tests need to move to functionaltests , since for resolving boms the Gradle proper Resolver is used"
@Ignore()
class SortedPatchFileListArtifactVersionResolverTest extends Specification {

    @Shared
    def source = new File("src/integrationTest/resources/patchresolvertests")

    @Ignore()
    def "getVersion from single Patchfile"() {
        given:
        def patchFile = new File(source,"PatchZ5401.json")
        def resolver = ResolverBuilderKt.create(PatchFileListVersionResolverBuilder.class)
                .add(patchFile)
                .build()
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"
    }

    @Ignore()
    def "getVersion from two Patchfiles sorted asc by Patchnummer with same Artifact"() {
        given:
        def resolver = ResolverBuilderKt.create(PatchFileListVersionResolverBuilder.class).parentDir(source)
                .add("PatchB5402.json")
                .add("PatchZ5401.json")
                .build()
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"
    }

    def "getVersion from two Patchfiles sorted desc Patchnummer with same Artifact"() {
        given:
        def resolver = ResolverBuilderKt.create(PatchFileListVersionResolverBuilder.class).parentDir(source)
                .add("PatchB5402.json")
                .add("PatchZ5401.json")
                .patchComparator(SortedPatchFileListVersionResolver.PatchComparator.PATCHNUMBER_DESC)
                .build()
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "xxxxxxxxxx"
    }

    def "getVersion from two Patchfiles sorted asc by TagNr with same Artifact"() {
        given:
        def resolver = ResolverBuilderKt.create(PatchFileListVersionResolverBuilder.class).parentDir(source)
                .add("PatchB5402.json")
                .add("PatchZ5401.json")
                .patchComparator(SortedPatchFileListVersionResolver.PatchComparator.TAGNR_ASC)
                .build()
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "xxxxxxxxxx"
    }

    def "getVersion from two Patchfiles sorted desc TagNr with same Artifact"() {
        given:
        def resolver = ResolverBuilderKt.create(PatchFileListVersionResolverBuilder.class).parentDir(source)
                .add("PatchB5402.json")
                .add("PatchZ5401.json")
                .patchComparator(SortedPatchFileListVersionResolver.PatchComparator.TAGNR_DESC)
                .build()
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"
    }


}
