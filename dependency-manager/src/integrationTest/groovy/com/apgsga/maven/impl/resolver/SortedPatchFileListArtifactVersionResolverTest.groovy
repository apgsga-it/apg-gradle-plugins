package com.apgsga.maven.impl.resolver

import spock.lang.Shared
import spock.lang.Specification

class SortedPatchFileListArtifactVersionResolverTest extends Specification {

    @Shared
    def source = new File("src/integrationTest/resources/patchresolvertests")

    def "getVersion from single Patchfile"() {
        given:
        def patchFile = new File(source,"PatchZ5401.json")
        def patchFileList = [patchFile] as Collection<File>
        def resolver = new SortedPatchFileListArtifactVersionResolver(patchFileList, SortedPatchFileListArtifactVersionResolver.PatchComparator.PATCHNUMBER_ASC)
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"
    }

    def "getVersion from two Patchfiles sorted asc by Patchnummer with same Artifact"() {
        given:
        def patchFileOne = new File(source,"PatchB5402.json")
        def patchFileTwo = new File(source,"PatchZ5401.json")
        def patchFileList = [patchFileOne,patchFileTwo] as Collection<File>
        def resolver = new SortedPatchFileListArtifactVersionResolver(patchFileList, SortedPatchFileListArtifactVersionResolver.PatchComparator.PATCHNUMBER_ASC)
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"
    }

    def "getVersion from two Patchfiles sorted desc Patchnummer with same Artifact"() {
        given:
        def patchFileOne = new File(source,"PatchB5402.json")
        def patchFileTwo = new File(source,"PatchZ5401.json")
        def patchFileList = [patchFileOne,patchFileTwo] as Collection<File>
        def resolver = new SortedPatchFileListArtifactVersionResolver(patchFileList,SortedPatchFileListArtifactVersionResolver.PatchComparator.PATCHNUMBER_DESC)
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "xxxxxxxxxx"
    }

    def "getVersion from two Patchfiles sorted asc by TagNr with same Artifact"() {
        given:
        def patchFileOne = new File(source,"PatchB5402.json")
        def patchFileTwo = new File(source,"PatchZ5401.json")
        def patchFileList = [patchFileOne,patchFileTwo] as Collection<File>
        def resolver = new SortedPatchFileListArtifactVersionResolver(patchFileList, SortedPatchFileListArtifactVersionResolver.PatchComparator.TAGNR_ASC)
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "xxxxxxxxxx"
    }

    def "getVersion from two Patchfiles sorted desc TagNr with same Artifact"() {
        given:
        def patchFileOne = new File(source,"PatchB5402.json")
        def patchFileTwo = new File(source,"PatchZ5401.json")
        def patchFileList = [patchFileOne,patchFileTwo] as Collection<File>
        def resolver = new SortedPatchFileListArtifactVersionResolver(patchFileList,SortedPatchFileListArtifactVersionResolver.PatchComparator.TAGNR_DESC)
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"
    }
}
