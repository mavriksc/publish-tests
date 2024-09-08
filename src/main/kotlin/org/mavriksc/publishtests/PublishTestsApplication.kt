package org.mavriksc.publishtests

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class PublishTestsApplication(val publisher: Publisher) : CommandLineRunner {
    override fun run(vararg args: String?) {
        publisher.publish()
    }
}

fun main(args: Array<String>) {
    runApplication<PublishTestsApplication>(*args).close()
}
