package org.example.countryguesser.scheduler

import org.example.countryguesser.service.CountrySyncService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class CountrySyncScheduler(
    private val countrySyncService: CountrySyncService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "\${jobs.cronSchedule}")
    fun syncCountries() {
        logger.info("Starting scheduled country synchronization")

        try {
            countrySyncService.syncCountries()
            logger.info("Scheduled country synchronization completed")
        } catch (exception: Exception) {
            logger.error("Scheduled country synchronization failed", exception)
        }
    }
}