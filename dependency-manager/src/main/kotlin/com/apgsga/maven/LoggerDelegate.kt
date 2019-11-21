package com.apgsga.maven

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}

fun getLogger(forClass: Class<*>): Logger =
        Logging.getLogger(forClass)


class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
    override fun getValue(thisRef: R, property: KProperty<*>): Logger = getLogger(getClassForLogging(thisRef.javaClass))
}
