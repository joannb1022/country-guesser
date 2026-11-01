package org.example.countryguesser.repository

import org.example.countryguesser.entity.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<Country, Long> {
    fun findByAlpha2Code(alpha2Code: String): Country?
    fun existsByAlpha2Code(alpha2Code: String): Boolean
    fun findAllByActiveTrueAndAlpha2CodeNotIn(alpha2Codes: Collection<String>): List<Country>
    fun findAllByAlpha2CodeIn(alpha2Codes: Collection<String>): List<Country>
}