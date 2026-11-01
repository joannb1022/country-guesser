package org.example.countryguesser.service

import org.example.countryguesser.client.RestCountriesClient
import org.example.countryguesser.repository.CountryRepository
import org.springframework.stereotype.Service

@Service
class CountrySyncService(
    private val restCountriesClient: RestCountriesClient,
    private val countryRepository: CountryRepository
) {

    //TODO TRANSACTIONAL?
    fun syncCountries() {
        // 1. Fetch countries
        // 2. Map DTOs to entities
        // 3. Save entities
    }
}