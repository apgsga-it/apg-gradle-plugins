package com.apgsga.testapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@SpringBootApplication
open class TestappApplication
fun main(args: Array<String>) {
    runApplication<TestappApplication>(*args)
}

data class EchoMessage (val message: String)

@RestController
@RequestMapping(path = ["service"])
open class EchoController {

    private val atomicCounter = AtomicLong()

    @RequestMapping(value = ["/echo"])
    fun echo(@RequestParam(value = "text", defaultValue = "Hello World") text: String): String {
        return getMessage(text).message
    }

    @RequestMapping(value = ["/echo/json"], method = [RequestMethod.GET], produces = ["application/json"])
    fun echoJson(@RequestParam(value = "text", defaultValue = "Hello World") text: String): EchoMessage {
        return getMessage(text)
    }

    private fun getMessage(text: String): EchoMessage {
        return EchoMessage(
                String.format("%d: %s", atomicCounter.incrementAndGet(), text))
    }

}