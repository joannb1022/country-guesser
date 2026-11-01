package org.example.countryguesser.entity

import jakarta.persistence.*

@Entity
@Table(name = "country")
class Country(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "alpha_2_code", nullable = false, unique = true)
    var alpha2Code: String,

    @Column(name = "alpha_3_code")
    var alpha3Code: String?,

    @Column(name = "name_common", nullable = false)
    var nameCommon: String,

    @Column(name = "name_official", nullable = false)
    var nameOfficial: String,

    @Column(name = "capital")
    var capital: String?,

    @Column(name = "region", nullable = false)
    var region: String,

    @Column(name = "subregion")
    var subregion: String?,

    @Column(name = "population", nullable = false)
    var population: Long,

    @Column(name = "landlocked", nullable = false)
    var landlocked: Boolean,

    @Column(name = "active", nullable = false)
    var active: Boolean = true
)