package com.transportcompany.transport_app.model

import jakarta.persistence.*

@Entity
@Table(name = "trip_state")
data class TripState(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String? = null
) 