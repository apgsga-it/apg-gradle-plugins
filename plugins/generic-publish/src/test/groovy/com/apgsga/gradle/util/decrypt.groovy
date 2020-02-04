package com.apgsga.gradle.util

import org.jasypt.util.text.BasicTextEncryptor

BasicTextEncryptor textEncryptor = new BasicTextEncryptor()
println "Encrypt Password"
textEncryptor.setPassword(System.in.newReader().readLine())
println "Encrypted Password?"
println textEncryptor.decrypt(System.in.newReader().readLine())
