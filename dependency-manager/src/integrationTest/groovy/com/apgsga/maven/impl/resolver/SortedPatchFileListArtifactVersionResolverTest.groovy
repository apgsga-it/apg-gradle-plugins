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
        def resolver = new SortedPatchFileListArtifactVersionResolver(patchFileList,true)
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"
    }

    def "getVersion from two Patchfiles sorted asc with same Artifact"() {
        given:
        def patchFileOne = new File(source,"PatchB5402.json")
        def patchFileTwo = new File(source,"PatchZ5401.json")
        def patchFileList = [patchFileOne,patchFileTwo] as Collection<File>
        def resolver = new SortedPatchFileListArtifactVersionResolver(patchFileList,true)
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "AAAAAAAAA"
    }

    def "getVersion from two Patchfiles sorted desc with same Artifact"() {
        given:
        def patchFileOne = new File(source,"PatchB5402.json")
        def patchFileTwo = new File(source,"PatchZ5401.json")
        def patchFileList = [patchFileOne,patchFileTwo] as Collection<File>
        def resolver = new SortedPatchFileListArtifactVersionResolver(patchFileList,false)
        when:
        def version = resolver.getVersion("com.affichage.it21.vk","zentraldispo-dao")
        then:
        version == "xxxxxxxxxx"
    }
}
