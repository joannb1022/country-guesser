package org.example.countryguesser.service

import org.example.countryguesser.client.RestCountriesClient
import org.springframework.stereotype.Service

@Service
class CountrySyncService(
    private val restCountriesClient: RestCountriesClient,
    private val countryPersistenceService: CountryPersistenceService
) {

    fun syncCountries() {
        val validCountries = restCountriesClient.getAllCountries()
            .filter { it.codes.alpha_2.isNotBlank() }

        require(validCountries.isNotEmpty()) {
            "No valid countries received. Skipping synchronization."
        }

        countryPersistenceService.reconcileCountries(validCountries)
    }
}