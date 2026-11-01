package org.example.countryguesser.client

import org.example.countryguesser.dto.CountriesResponse
import org.example.countryguesser.dto.CountryDto
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class RestCountriesClient(
    private val restCountriesRestClient: RestClient
) {

    fun getAllCountries(): List<CountryDto>{
        val countries = mutableListOf<CountryDto>()

        val limit = 100
        var offset = 0
        var hasMore = true

        while(hasMore){
            val response = restCountriesRestClient.get().
                uri { uriBuilder ->
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

            countries.addAll(response.data.objects)

            hasMore = response.data.meta.more
            offset += response.data.meta.count
        }
        return countries
    }
}