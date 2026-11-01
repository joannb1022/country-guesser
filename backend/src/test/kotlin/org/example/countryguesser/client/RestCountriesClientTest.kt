package org.example.countryguesser.client

import org.junit.jupiter.api.BeforeEach
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestClient
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import kotlin.test.assertEquals

class RestCountriesClientTest {

    private lateinit var client: RestCountriesClient
    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun setUp() {
        val builder = RestClient.builder()
            .baseUrl("https://api.restcountries.com/countries/v5")

        mockServer = MockRestServiceServer
            .bindTo(builder)
            .build()

        client = RestCountriesClient(builder.build())
    }


    @Test
    fun `should return countries from API`() {
        val response = """
                {
          "data": {
            "objects": [
              {
                "names": {
                  "common": "Japan",
                  "official": "Japan"
                },
                "codes": {
                  "alpha_2": "JP",
                  "alpha_3": "JPN"
                },
                "capitals": [
                  {
                    "name": "Tokyo"
                  }
                ],
                "region": "Asia",
                "subregion": "Eastern Asia",
                "population": 125000000,
                "landlocked": false
              }
            ],
            "meta": {
              "total": 249,
              "count": 1,
              "limit": 100,
              "offset": 0,
              "more": false
            }
          }
        }
        """.trimIndent()

        mockServer.expect(
            requestTo(
                "https://api.restcountries.com/countries/v5?limit=100&offset=0"
            )
        )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withSuccess(response, MediaType.APPLICATION_JSON)
            )

        val countries = client.getAllCountries()

        assertEquals(1, countries.size)
        assertEquals("Japan", countries[0].names.common)
        assertEquals("JP", countries[0].codes.alpha_2)

        mockServer.verify()
    }
}
