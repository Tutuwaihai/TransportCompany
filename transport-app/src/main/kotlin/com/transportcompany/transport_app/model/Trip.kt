package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "trip")
data class Trip(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "description")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcityfrom")
    var cityFrom: City,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcityto")
    var cityTo: City,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idwarefrom")
    var wareHouseFrom: Warehouse? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idwareto")
    var wareHouseTo: Warehouse? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtriptype")
    var tripType: TripType,

    @Column(name = "dispatchdate")
    var dispatchDate: Date? = null,

    @Column(name = "arrivaldate")
    var arrivalDate: Date? = null,

    @Column(name = "expecteddate")
    var expectedDate: Date? = null,

    @Column(name = "docnum")
    var docNum: String,

    @Column(name = "is_transit")
    var isTransit: Int,

    @Column(name = "state")
    var state: Int,

    @Column(name = "costs")
    var costs: BigDecimal,

    @Column(name = "is_city_costs")
    var isCityCosts: Int,

    @Column(name = "idemployee")
    var idEmployee: Long? = null,

    @Column(name = "idtransport")
    var idTransport: Long? = null,

    @Column(name = "cargo_seal")
    var cargoSeal: String? = null,

    @Column(name = "createdate")
    val createDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "modifydate")
    var modifyDate: LocalDateTime? = null,

    @Column(name = "createuser")
    val createUser: Long? = null,

    @Column(name = "modifyuser")
    var modifyUser: Long? = null,

    @Column(name = "isdeleted")
    var isDeleted: Int
)
