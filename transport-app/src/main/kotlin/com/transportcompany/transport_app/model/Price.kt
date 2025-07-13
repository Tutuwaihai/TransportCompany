package com.transportcompany.transport_app.model

import jakarta.persistence.*

@Entity
@Table(name = "price")
data class Price(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "price_volume")
    val priceVolume: Double? = null
)