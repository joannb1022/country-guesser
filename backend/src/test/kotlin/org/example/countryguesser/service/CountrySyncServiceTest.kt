package org.example.countryguesser.service

import org.example.countryguesser.client.RestCountriesClient
import org.example.countryguesser.dto.CapitalDto
import org.example.countryguesser.dto.CountryCodes
import org.example.countryguesser.dto.CountryDto
import org.example.countryguesser.dto.CountryNames
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.junit.jupiter.api.Test

class CountrySyncServiceTest {

    private val restCountriesClient = mock(RestCountriesClient::class.java)
    private val countryPersistenceService =
        mock(CountryPersistenceService::class.java)

    private val service = CountrySyncService(
        restCountriesClient,
        countryPersistenceService
    )

    @Test
    fun `syncCountries should fetch countries and delegate them to persistence service`() {
        // given
        val poland = CountryDto(
            names = CountryNames(
                common = "Poland",
                official = "Republic of Poland"
            ),
            codes = CountryCodes(
                alpha_2 = "PL",
                alpha_3 = "POL"
            ),
            capitals = listOf(CapitalDto("Warsaw")),
            region = "Europe",
            subregion = "Central Europe",
            population = 38_000_000L,
            landlocked = false
        )

        `when`(restCountriesClient.getAllCountries())
            .thenReturn(listOf(poland))

        // when
        service.syncCountries()

        // then
        verify(restCountriesClient).getAllCountries()
        verify(countryPersistenceService).reconcileCountries(
            listOf(poland)
        )

        verifyNoMoreInteractions(countryPersistenceService)
    }

    @Test
    fun `syncCountries should filter countries with blank alpha2 codes`() {
        // given
        val poland = CountryDto(
            names = CountryNames("Poland", "Republic of Poland"),
            codes = CountryCodes("PL", "POL"),
            capitals = listOf(CapitalDto("Warsaw")),
            region = "Europe",
            subregion = "Central Europe",
            population = 38_000_000L,
            landlocked = false
        )

        val invalidCountry = CountryDto(
            names = CountryNames("Unknown", "Unknown"),
            codes = CountryCodes("", ""),
            capitals = emptyList(),
            region = "Europe",
            subregion = null,
            population = 1L,
            landlocked = false
        )

        `when`(restCountriesClient.getAllCountries())
            .thenReturn(listOf(poland, invalidCountry))

        // when
        service.syncCountries()

        // then
        verify(countryPersistenceService).reconcileCountries(
            listOf(poland)
        )
    }

    @Test
    fun `syncCountries should delegate an empty list when no valid countries are returned`() {
        // given
        val invalidCountry = CountryDto(
            names = CountryNames("Unknown", "Unknown"),
            codes = CountryCodes("", ""),
            capitals = emptyList(),
            region = "Europe",
            subregion = null,
            population = 1L,
            landlocked = false
        )

        `when`(restCountriesClient.getAllCountries())
            .thenReturn(listOf(invalidCountry))

        assertThrows(IllegalArgumentException::class.java) {
            service.syncCountries()
        }

    }
}
