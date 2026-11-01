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
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getAllCountries(): List<CountryDto> {
        val countries = mutableListOf<CountryDto>()

        val limit = 100
        var offset = 0
        var hasMore = true

        while (hasMore) {
            if (offset == 0) {
                logger.info("Starting calling Rest Countries API for all countries, limit $limit.")
            } else {
                logger.info("Getting more countries, offset $offset.")
                logger.info("Countries so far: ${countries.map { it.names.official }}")
            }


            try {
                val response = restCountriesRestClient.get()
                    .uri { uriBuilder ->
                        uriBuilder
                            .queryParam("limit", limit)
                            .queryParam("offset", offset)
                            .build()
                    }
                    .retrieve()
                    .body(CountriesResponse::class.java)
                    ?: throw IllegalStateException("Empty response from REST Countries API")

                countries.addAll(response.data.objects)

                hasMore = response.data.meta.more
                offset += response.data.meta.count

            } catch (ex: Exception) {
                logger.error("Failed to deserialize REST Countries response", ex)
                throw ex
            }
        }
        return countries
    }
}