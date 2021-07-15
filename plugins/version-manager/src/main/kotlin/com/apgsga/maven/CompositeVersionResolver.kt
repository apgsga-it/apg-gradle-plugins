package com.apgsga.maven

interface CompositeVersionResolver  : VersionResolver {
    fun add(order : Int, versionResolver: VersionResolver)
}