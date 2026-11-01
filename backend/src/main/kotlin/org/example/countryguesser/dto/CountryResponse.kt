package org.example.countryguesser.dto

import org.example.countryguesser.entity.Country

data class CountriesResponse(
    val data: CountryData
)

data class CountryData(
    val objects: List<CountryDto>,
    val meta: PaginationMeta
)

data class PaginationMeta(
    val total: Int,
    val count: Int,
    val limit: Int,
    val offset: Int,
    val more: Boolean
)

data class CountryDto(
    val names: CountryNames,
    val codes: CountryCodes,
    val capitals: List<CapitalDto> = emptyList(),
    val region: String,
    val subregion: String? = null,
    val population: Long,
    val landlocked: Boolean
) {

    //TODO move it to mapper?
    fun toEntity() = Country(
        alpha2Code = codes.alpha_2,
        alpha3Code = codes.alpha_3,
        nameCommon = names.common,
        nameOfficial = names.official,
        capital = capitals.firstOrNull()?.name,
        region = region,
        subregion = subregion,
        population = population,
        landlocked = landlocked,
        active = true
    )
}

data class CapitalDto(
    val name: String
)

data class CountryNames(
    val common: String,
    val official: String
)

data class CountryCodes(
    val alpha_2: String,
    val alpha_3: String
)