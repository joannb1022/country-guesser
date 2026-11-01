package org.example.countryguesser.client

import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertTrue

// tests actual connection with rest countries api
@SpringBootTest
@Tag("integration")
class RestCountriesClientIntegrationTest(
    @Autowired
    private val restCountriesClient: RestCountriesClient
) {

    @Test
    fun `should fetch countries from REST Countries API`() {
        val countries = restCountriesClient.getAllCountries()

        assertTrue(countries.isNotEmpty())

        assertTrue(
            countries.any { it.codes.alpha_2 == "JP" }
        )
    }
}