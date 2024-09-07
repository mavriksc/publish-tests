package org.mavriksc.publishtests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


// break out some files
// s3 config object that provides  connection and allows for commands
// actions steps
// if setup -> setup ( push web files create reports dir)
//


@SpringBootApplication
class PublishTestsApplication(val publisher: Publisher) : CommandLineRunner {
    override fun run(vararg args: String?) {
        publisher.publish()
    }
}

fun main(args: Array<String>) {
    runApplication<PublishTestsApplication>(*args)
}
