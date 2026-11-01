package org.example.countryguesser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CountryGuesserApplication

fun main(args: Array<String>) {
    runApplication<CountryGuesserApplication>(*args)
}
