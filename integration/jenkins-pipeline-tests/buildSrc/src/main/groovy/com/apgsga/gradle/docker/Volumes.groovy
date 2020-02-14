package com.apgsga.gradle.docker

class Volumes {

    static void convert(Map<String,String> volumes) {
        volumes.collectEntries { k, v ->
            if (k.contains('\\')) {
                k = "//${k.replaceAll('\\\\', '/')}"
                k = k.replaceAll(':', '')
            }
            [(k): v]
        }
    }

    static void hello(text) {
        println "************************ ${text}"
    }
}
