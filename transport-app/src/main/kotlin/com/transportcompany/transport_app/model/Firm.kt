package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "firms")
data class Firm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "modifydate")
    val modifyDate: LocalDateTime? = null,

    @Column(name = "createdate")
    val createDate: LocalDateTime? = null,

    @Column(name = "isdeleted")
    val isDeleted: Int,

    @Column(name = "title")
    val title: String,

    @Column(name = "fulltitle")
    val fullTitle: String? = null,

    @Column(name = "inn")
    val inn: String? = null,

    @Column(name = "kpp")
    val kpp: String? = null,

    @Column(name = "phone")
    val phone: String? = null,

    @Column(name = "juraddress")
    val jurAddress: String? = null,

    @Column(name = "director")
    val director: String? = null,

    @Column(name = "ndspercent")
    val ndsPercent: Double? = null,

    @Column(name = "idcity")
    val idCity: Long? = null,

    @Column(name = "factaddress")
    val factAddress: String? = null,

    @Column(name = "bank_title")
    val bankTitle: String? = null,

    @Column(name = "type")
    val type: Int? = null,

    @Column(name = "email")
    val email: String? = null
)
