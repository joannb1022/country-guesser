package org.example.countryguesser.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Configuration
class RestCountriesConfig {

    @Bean
    fun restCountriesRestClient(
        @Value("\${rest-countries.base-url}") baseUrl: String,
        @Value("\${rest-countries.api-key}") apiKey: String
    ): RestClient {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(
                HttpHeaders.AUTHORIZATION,
                "Bearer $apiKey"
            )
            .defaultHeader(
                HttpHeaders.ACCEPT,
                MediaType.APPLICATION_JSON_VALUE
            )
            .build()
    }
}