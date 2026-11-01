package org.example.countryguesser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CountryGuesserApplication

fun main(args: Array<String>) {
    runApplication<CountryGuesserApplication>(*args)
}
