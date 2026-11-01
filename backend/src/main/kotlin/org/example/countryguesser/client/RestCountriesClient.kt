package org.example.countryguesser.client

import org.example.countryguesser.dto.CountriesResponse
import org.example.countryguesser.dto.CountryDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class RestCountriesClient(
    private val restCountriesRestClient: RestClient
) {
    private val logger = LoggerFactory.getLogger(RestCountriesClient::class.java)

    fun getAllCountries(): List<CountryDto> {
        val countries = mutableListOf<CountryDto>()

        val limit = 100
        var offset = 0

        while (true) {
            logger.info(
                "Fetching countries: offset={}, limit={}",
                offset,
                limit
            )

            val response = restCountriesRestClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build()
                }
                .retrieve()
                .body(CountriesResponse::class.java)
                ?: throw IllegalStateException(
                    "Empty response from REST Countries API"
                )

            val page = response.data
            val meta = page.meta

            countries.addAll(page.objects)

            logger.info(
                "Received country page: offset={}, count={}, total={}, more={}",
                meta.offset,
                meta.count,
                meta.total,
                meta.more
            )

            if (!meta.more) {
                break
            }

            offset += meta.count
        }

        logger.info("Successfully fetched {} countries", countries.size)

        return countries
    }
}