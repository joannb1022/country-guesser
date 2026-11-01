package org.example.countryguesser.scheduler

import org.example.countryguesser.service.CountrySyncService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CountrySyncScheduler(
    private val countrySyncService: CountrySyncService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

//    @Scheduled(
//        cron = "0 0 3 * * MON",
//        zone = "Europe/Warsaw"
//    )
    @Scheduled(fixedDelay = 60_000)
    fun syncCountries() {
        logger.info("Starting scheduled country synchronization")

        countrySyncService.syncCountries()

        logger.info("Scheduled country synchronization completed")
    }
}