package com.apgsga.gradle.util

import org.jasypt.util.text.BasicTextEncryptor

BasicTextEncryptor textEncryptor = new BasicTextEncryptor()
println "Encrypt Password"
textEncryptor.setPassword(System.in.newReader().readLine())
println "Password to Encrypt?"
println textEncryptor.encrypt(System.in.newReader().readLine())
