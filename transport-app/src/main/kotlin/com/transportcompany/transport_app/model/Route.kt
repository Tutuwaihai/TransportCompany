package com.transportcompany.transport_app.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "route")
data class Route(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "modifydate")
    val modifydate: LocalDateTime? = null,
    @Column(name = "createdate")
    val createdate: LocalDateTime? = null,

    @Column(name = "isdeleted")
    val isDeleted: Int = 0,
    @Column(name = "title")
    val title: String,
    @Column(name = "description")
    val description: String? = null,

    @Column(name = "delivery_time")
    val deliveryTime: Int? = null,

    @Column(name = "idcityfrom")
    val idCityFrom: Long? = null,

    @Column(name = "idcityto")
    val idCityTo: Long? = null,

    @Column(name = "idtriptype")
    val idTripType: Long? = null
)
