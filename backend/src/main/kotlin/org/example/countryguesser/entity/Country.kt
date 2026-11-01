package org.example.countryguesser.entity

import jakarta.persistence.*

@Entity
@Table(name = "country")
class Country(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "alpha_2_code", nullable = false, unique = true)
    val alpha2Code: String,

    @Column(name = "alpha_3_code")
    val alpha3Code: String?,

    @Column(name = "name_common", nullable = false)
    val nameCommon: String,

    @Column(name = "name_official", nullable = false)
    val nameOfficial: String,

    @Column(name = "capital")
    val capital: String?,

    @Column(name = "region", nullable = false)
    val region: String,

    @Column(name = "subregion")
    val subregion: String?,

    @Column(name = "population", nullable = false)
    val population: Long,

    @Column(name = "landlocked", nullable = false)
    val landlocked: Boolean
)