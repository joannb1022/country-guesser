package org.example.countryguesser.service

import org.example.countryguesser.dto.CountryDto
import org.example.countryguesser.repository.CountryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CountryPersistenceService(
    private val countryRepository: CountryRepository
) {

    @Transactional
    fun reconcileCountries(incomingCountries: List<CountryDto>) {
        val incomingCodes = incomingCountries
            .map { it.codes.alpha_2 }
            .toSet()

        val existingByCode = countryRepository
            .findAllByAlpha2CodeIn(incomingCodes)
            .associateBy { it.alpha2Code }

        val countriesToSave = incomingCountries.map { dto ->
            val existing = existingByCode[dto.codes.alpha_2]

            if (existing != null) {
                existing.apply {
                    alpha3Code = dto.codes.alpha_3
                    nameCommon = dto.names.common
                    nameOfficial = dto.names.official
                    capital = dto.capitals.firstOrNull()?.name
                    region = dto.region
                    subregion = dto.subregion
                    population = dto.population
                    landlocked = dto.landlocked
                    active = true
                }
            } else {
                dto.toEntity()
            }
        }

        countryRepository.saveAll(countriesToSave)

        deactivateMissingCountries(incomingCodes)
    }

    private fun deactivateMissingCountries(incomingCodes: Set<String>) {
        val missingCountries =
            countryRepository.findAllByActiveTrueAndAlpha2CodeNotIn(incomingCodes)

        missingCountries.forEach { it.active = false }
    }
}