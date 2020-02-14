package com.apgsga.gradle.docker

class Volumes {

    static Map<String,String> convert(Map<String,String> volumes) {
        def result = volumes.collectEntries { k, v ->
            if (k.contains('\\')) {
                k = "//${k.replaceAll('\\\\', '/')}"
                k = k.replaceAll(':', '')
            }
            println ("Bind, key: ${k}, value : ${v}")
            [(k): v]
        }
        result
    }

    static void hello(text) {
        println "************************ ${text}"
    }
}
