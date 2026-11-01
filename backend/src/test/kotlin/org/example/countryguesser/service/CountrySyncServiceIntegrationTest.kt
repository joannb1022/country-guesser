package org.example.countryguesser.service

import org.example.countryguesser.client.RestCountriesClient
import org.example.countryguesser.dto.CapitalDto
import org.example.countryguesser.dto.CountryCodes
import org.example.countryguesser.dto.CountryDto
import org.example.countryguesser.dto.CountryNames
import org.example.countryguesser.entity.Country
import org.example.countryguesser.repository.CountryRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
class CountrySyncServiceIntegrationTest {

    @Autowired
    private lateinit var countryRepository: CountryRepository

    @Autowired
    private lateinit var countrySyncService: CountrySyncService

    @MockitoBean
    private lateinit var restCountriesClient: RestCountriesClient

    @BeforeEach
    fun setup() {
        countryRepository.deleteAll()
    }

    @Test
    fun `should update existing country by alpha2 code and deactivate missing ones`() {
        // given existing data in DB
        val poland = Country(
            alpha2Code = "PL",
            alpha3Code = "POL",
            nameCommon = "Old Poland",
            nameOfficial = "Old Official Poland",
            capital = "Old Warsaw",
            region = "Europe",
            subregion = "Central Europe",
            population = 1L,
            landlocked = true,
            active = true
        )

        val germany = Country(
            alpha2Code = "DE",
            alpha3Code = "DEU",
            nameCommon = "Germany",
            nameOfficial = "Germany Official",
            capital = "Berlin",
            region = "Europe",
            subregion = "Central Europe",
            population = 83_000_000L,
            landlocked = false,
            active = true
        )

        val france = Country(
            alpha2Code = "FR",
            alpha3Code = "FRA",
            nameCommon = "France",
            nameOfficial = "France Official",
            capital = "Paris",
            region = "Europe",
            subregion = "Western Europe",
            population = 67_000_000L,
            landlocked = false,
            active = true
        )

        countryRepository.saveAll(listOf(poland, germany, france))

        // incoming API data: only PL is present
        given(restCountriesClient.getAllCountries()).willReturn(
            listOf(
                CountryDto(
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
            )
        )

        // when
        countrySyncService.syncCountries()

        // then: existing country updated
        val updatedPoland = countryRepository.findByAlpha2Code("PL")
        assertNotNull(updatedPoland)
        assertEquals("Poland", updatedPoland.nameCommon)
        assertEquals("Republic of Poland", updatedPoland.nameOfficial)
        assertEquals("Warsaw", updatedPoland.capital)
        assertEquals(38_000_000L, updatedPoland.population)
        assertTrue(updatedPoland.active)

        // then: countries missing from incoming data are deactivated
        val de = countryRepository.findByAlpha2Code("DE")
        assertNotNull(de)
        assertFalse(de.active)

        val fr = countryRepository.findByAlpha2Code("FR")
        assertNotNull(fr)
        assertFalse(fr.active)
    }
}